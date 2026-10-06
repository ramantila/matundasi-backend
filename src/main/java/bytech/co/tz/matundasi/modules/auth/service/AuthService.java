package bytech.co.tz.matundasi.modules.auth.service;

import bytech.co.tz.matundasi.common.security.JwtService;
import bytech.co.tz.matundasi.modules.auth.payload.LoginRequest;
import bytech.co.tz.matundasi.modules.auth.payload.RefreshTokenRequest;
import bytech.co.tz.matundasi.modules.auth.response.LoginResponse;
import bytech.co.tz.matundasi.modules.permissions.entity.Permission;
import bytech.co.tz.matundasi.modules.rolepermissions.entity.RolePermission;
import bytech.co.tz.matundasi.modules.rolepermissions.repository.RolePermissionRepository;
import bytech.co.tz.matundasi.modules.roles.entity.Role;
import bytech.co.tz.matundasi.modules.users.entity.User;
import bytech.co.tz.matundasi.modules.users.entity.UserStatus;
import bytech.co.tz.matundasi.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String INVALID_CREDENTIALS = "Invalid username or password";

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Unknown username and wrong password give the same answer, so the
        // response doesn't reveal which usernames exist.
        User user = userRepository.findByUsernameIgnoreCaseAndDeletedAtIsNull(request.username().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS);
        }
        requireActive(user);
        return issueSession(user);
    }

    // Swaps a refresh token for a new access + refresh token pair. Roles and
    // permissions are re-read, so changes to them apply from the next refresh.
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        UUID userId = refreshTokenService.consume(request.refreshToken());
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));
        requireActive(user);
        return issueSession(user);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revoke(request.refreshToken());
    }

    private void requireActive(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User account is " + user.getStatus().name().toLowerCase());
        }
    }

    private LoginResponse issueSession(User user) {
        List<Role> roles = user.getRoles().stream()
                .filter(role -> !role.isDeleted() && role.isActive())
                .sorted(Comparator.comparing(Role::getName))
                .toList();

        List<String> permissions = roles.isEmpty()
                ? List.of()
                : rolePermissionRepository.findAllByRole_IdInAndDeletedAtIsNullAndPermission_DeletedAtIsNull(
                                roles.stream().map(Role::getId).toList())
                        .stream()
                        .map(RolePermission::getPermission)
                        .map(Permission::getName)
                        .distinct()
                        .sorted()
                        .toList();

        Instant expiresAt = jwtService.nextExpiry();
        String token = jwtService.generateToken(user.getId(), user.getUsername(), permissions, expiresAt);
        RefreshTokenService.Issued refresh = refreshTokenService.issue(user);

        return new LoginResponse(
                token,
                "Bearer",
                expiresAt,
                refresh.token(),
                refresh.expiresAt(),
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPhone(),
                user.getBranch().getId(),
                user.getBranch().getName(),
                roles.stream().map(Role::getName).collect(Collectors.joining(", ")),
                roles.stream().map(role -> new LoginResponse.RoleSummary(role.getId(), role.getName())).toList(),
                permissions
        );
    }
}
