package bytech.co.tz.matundasi.modules.rolepermissions.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RolePermissionRequest(
        @NotNull(message = "role_id is required")
        UUID roleId,

        @NotNull(message = "permission_id is required")
        UUID permissionId

)
{}
