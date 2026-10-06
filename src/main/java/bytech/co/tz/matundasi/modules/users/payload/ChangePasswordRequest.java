package bytech.co.tz.matundasi.modules.users.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Admin-driven reset — no current password check.
public record ChangePasswordRequest(
        @NotBlank(message = "new_password is required")
        @Size(min = 8, message = "new_password must be at least 8 characters")
        String newPassword

)
{}
