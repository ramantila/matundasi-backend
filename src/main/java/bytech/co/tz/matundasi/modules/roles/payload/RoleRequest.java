package bytech.co.tz.matundasi.modules.roles.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record RoleRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100)
        String name,

        String description,

        @NotNull(message = "permission_ids is required")
        Set<UUID> permissionIds,

        // Omitted on create → defaults to true (active). Omitted on update
        // → leaves the existing value untouched.
        Boolean active

)
{}
