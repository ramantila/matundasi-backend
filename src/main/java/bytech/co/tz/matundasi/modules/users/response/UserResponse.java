package bytech.co.tz.matundasi.modules.users.response;

import bytech.co.tz.matundasi.modules.users.entity.UserStatus;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID branchId,
        String branchName,
        String firstName,
        String middleName,
        String lastName,
        String phone,
        Set<UUID> roleIds,
        Set<String> roleNames,
        String username,
        UserStatus status
) {}
