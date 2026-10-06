package bytech.co.tz.matundasi.modules.permissions.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100)
        String name

)
{}
