package bytech.co.tz.matundasi.modules.users.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record UserRequest(
        @NotNull(message = "branch_id is required")
        UUID branchId,

        @NotBlank(message = "first_name is required")
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String middleName,

        @NotBlank(message = "last_name is required")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "phone is required")
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "phone number is invalid")
        String phone,

        Set<UUID> roleIds,

        @NotBlank(message = "username is required")
        @Size(min = 3, max = 100)
        String username,

        // Required on create (enforced in UserService); ignored on update —
        // use PATCH /users/{id}/change-password to change it.
        @Size(min = 8, message = "password must be at least 8 characters")
        String password

)
{}
