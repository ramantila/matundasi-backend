package bytech.co.tz.matundasi.modules.rolepermissions.response;

import java.util.UUID;

public record RolePermissionResponse(
        UUID id,
        UUID roleId,
        String roleName,
        UUID permissionId,
        String permissionName
) {}
