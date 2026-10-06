package bytech.co.tz.matundasi.modules.auth.service;

import bytech.co.tz.matundasi.modules.auth.entity.RefreshToken;
import bytech.co.tz.matundasi.modules.auth.repository.RefreshTokenRepository;
import bytech.co.tz.matundasi.modules.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String INVALID_REFRESH_TOKEN = "Refresh token is invalid or expired";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public record Issued(String token, Instant expiresAt) {}

    @Transactional
    public Issued issue(User user) {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        Instant expiresAt = Instant.now().plusMillis(refreshExpirationMs);

        refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .tokenHash(hash(token))
                .expiresAt(expiresAt)
                .build());
        return new Issued(token, expiresAt);
    }

    // Revokes the token and returns its user's id; the caller issues the replacement.
    // A token that was already used, revoked or expired is rejected with 401.
    @Transactional
    public UUID consume(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hash(token))
                .filter(t -> !t.isRevoked() && !t.isExpired())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_REFRESH_TOKEN));

        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getUser().getId();
    }

    // Logout: unknown or already-revoked tokens are ignored so logging out never fails.
    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByTokenHash(hash(token))
                .filter(t -> !t.isRevoked())
                .ifPresent(t -> {
                    t.setRevokedAt(Instant.now());
                    refreshTokenRepository.save(t);
                });
    }

    // Ends every session of a user (password reset, account deleted).
    @Transactional
    public void revokeAll(UUID userId) {
        Instant now = Instant.now();
        for (RefreshToken t : refreshTokenRepository.findAllByUser_IdAndRevokedAtIsNull(userId)) {
            t.setRevokedAt(now);
            refreshTokenRepository.save(t);
        }
    }

    private static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
