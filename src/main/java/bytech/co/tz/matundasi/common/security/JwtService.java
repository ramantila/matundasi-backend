package bytech.co.tz.matundasi.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {
    private static final String USERNAME_CLAIM = "username";
    private static final String PERMISSIONS_CLAIM = "permissions";

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    public String generateToken(UUID userId, String username, List<String> permissions, Instant expiresAt) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim(USERNAME_CLAIM, username)
                .claim(PERMISSIONS_CLAIM, permissions)
                .issuedAt(new Date())
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    public Instant nextExpiry() {
        return Instant.now().plusMillis(expirationMs);
    }

    // Empty when the token is malformed, tampered with or expired.
    public Optional<Claims> parse(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public String userId(Claims claims) {
        return claims.getSubject();
    }

    public String username(Claims claims) {
        return claims.get(USERNAME_CLAIM, String.class);
    }

    public List<String> permissions(Claims claims) {
        List<?> raw = claims.get(PERMISSIONS_CLAIM, List.class);
        return raw == null ? List.of() : raw.stream().map(String::valueOf).toList();
    }
}
