package bytech.co.tz.matundasi.modules.users.service;

import bytech.co.tz.matundasi.modules.auth.service.RefreshTokenService;
import bytech.co.tz.matundasi.modules.branch.entity.Branch;
import bytech.co.tz.matundasi.modules.branch.repository.BranchRepository;
import bytech.co.tz.matundasi.modules.roles.entity.Role;
import bytech.co.tz.matundasi.modules.roles.repository.RoleRepository;
import bytech.co.tz.matundasi.modules.users.entity.User;
import bytech.co.tz.matundasi.modules.users.payload.ChangePasswordRequest;
import bytech.co.tz.matundasi.modules.users.payload.UserRequest;
import bytech.co.tz.matundasi.modules.users.repository.UserRepository;
import bytech.co.tz.matundasi.modules.users.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponse create(UserRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is required");
        }
        String username = request.username().trim();
        String phone = request.phone().trim();

        if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNull(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        if (userRepository.existsByPhoneAndDeletedAtIsNull(phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone is already registered to another user");
        }

        User user = new User();
        apply(user, username, phone, request);
        user.setPassword(passwordEncoder.encode(request.password()));

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAllByDeletedAtIsNullOrderByFirstNameAscLastNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return toResponse(getActiveUser(id));
    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User user = getActiveUser(id);
        String username = request.username().trim();
        String phone = request.phone().trim();

        if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNullAndIdNot(username, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        if (userRepository.existsByPhoneAndDeletedAtIsNullAndIdNot(phone, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone is already registered to another user");
        }

        apply(user, username, phone, request);

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        User user = getActiveUser(id);
        user.softDelete();
        userRepository.save(user);
        refreshTokenService.revokeAll(id);
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = getActiveUser(id);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        // Signs the user out everywhere once their current access token expires.
        refreshTokenService.revokeAll(id);
    }

    private void apply(User user, String username, String phone, UserRequest request) {
        user.setBranch(getActiveBranch(request.branchId()));
        user.setFirstName(request.firstName().trim());
        user.setMiddleName(trimToNull(request.middleName()));
        user.setLastName(request.lastName().trim());
        user.setPhone(phone);
        user.setRoles(resolveRoles(request.roleIds()));
        user.setUsername(username);
    }

    private Set<Role> resolveRoles(Set<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Role> resolved = new HashSet<>();
        for (UUID roleId : roleIds) {
            resolved.add(roleRepository.findByIdAndDeletedAtIsNull(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId)));
        }
        return resolved;
    }

    private User getActiveUser(UUID id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Branch getActiveBranch(UUID branchId) {
        return branchRepository.findByIdAndDeletedAtIsNull(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private UserResponse toResponse(User user) {
        Set<Role> roles = user.getRoles().stream()
                .filter(role -> !role.isDeleted())
                .collect(Collectors.toSet());
        return new UserResponse(
                user.getId(),
                user.getBranch().getId(),
                user.getBranch().getName(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPhone(),
                roles.stream().map(Role::getId).collect(Collectors.toSet()),
                roles.stream().map(Role::getName).collect(Collectors.toSet()),
                user.getUsername(),
                user.getStatus()
        );
    }
}
