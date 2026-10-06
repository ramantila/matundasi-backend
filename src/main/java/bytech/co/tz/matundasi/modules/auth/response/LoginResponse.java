package bytech.co.tz.matundasi.modules.auth.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record LoginResponse(
        String token,
        String tokenType,
        Instant expiresAt,
        // Single-use: POST it to /auth/refresh for a new token pair before it expires.
        String refreshToken,
        Instant refreshExpiresAt,
        UUID userId,
        String username,
        String firstName,
        String middleName,
        String lastName,
        String phone,
        UUID branchId,
        String branchName,
        // All role names joined with ", " (what the frontend shows as the user's role).
        String role,
        List<RoleSummary> roles,
        List<String> permissions
) {
    public record RoleSummary(
            UUID id,
            String name
    ) {}
}
