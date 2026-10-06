package bytech.co.tz.matundasi.modules.roles.response;

import bytech.co.tz.matundasi.modules.permissions.response.PermissionResponse;

import java.util.List;
import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String description,
        boolean active,
        List<PermissionResponse> permissions
) {}
