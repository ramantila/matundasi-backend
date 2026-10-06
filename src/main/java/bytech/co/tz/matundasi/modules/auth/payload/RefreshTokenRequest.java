package bytech.co.tz.matundasi.modules.auth.payload;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "refresh_token is required")
        String refreshToken

)
{}
