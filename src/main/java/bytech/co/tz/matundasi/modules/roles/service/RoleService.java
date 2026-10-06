package bytech.co.tz.matundasi.modules.roles.service;

import bytech.co.tz.matundasi.modules.permissions.service.PermissionService;
import bytech.co.tz.matundasi.modules.rolepermissions.service.RolePermissionService;
import bytech.co.tz.matundasi.modules.roles.entity.Role;
import bytech.co.tz.matundasi.modules.roles.payload.RoleRequest;
import bytech.co.tz.matundasi.modules.roles.repository.RoleRepository;
import bytech.co.tz.matundasi.modules.roles.response.RoleResponse;
import bytech.co.tz.matundasi.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RolePermissionService rolePermissionService;
    private final UserRepository userRepository;

    @Transactional
    public RoleResponse create(RoleRequest request) {
        String name = request.name().trim();

        if (roleRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role name already exists");
        }

        Role role = Role.builder()
                .name(name)
                .description(trimToNull(request.description()))
                .active(request.active() == null || request.active())
                .build();
        role = roleRepository.save(role);
        rolePermissionService.replacePermissions(role, request.permissionIds());

        return toResponse(role);
    }

    // activeOnly=true is for the user create/edit role picker, so a
    // deactivated role can't be newly assigned.
    @Transactional(readOnly = true)
    public List<RoleResponse> findAll(boolean activeOnly) {
        List<Role> roles = activeOnly
                ? roleRepository.findAllByDeletedAtIsNullAndActiveTrueOrderByNameAsc()
                : roleRepository.findAllByDeletedAtIsNullOrderByNameAsc();
        return roles.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse findById(UUID id) {
        return toResponse(getActiveRole(id));
    }

    @Transactional
    public RoleResponse update(UUID id, RoleRequest request) {
        Role role = getActiveRole(id);
        String name = request.name().trim();

        if (roleRepository.existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(name, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role name already exists");
        }

        role.setName(name);
        role.setDescription(trimToNull(request.description()));
        if (request.active() != null) {
            role.setActive(request.active());
        }
        role = roleRepository.save(role);
        rolePermissionService.replacePermissions(role, request.permissionIds());

        return toResponse(role);
    }

    @Transactional
    public void delete(UUID id) {
        Role role = getActiveRole(id);

        if (userRepository.existsByRoles_IdAndDeletedAtIsNull(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Role is still assigned to one or more users");
        }

        rolePermissionService.removeAllForRole(id);
        role.softDelete();
        roleRepository.save(role);
    }

    public Role getActiveRole(UUID id) {
        return roleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isActive(),
                rolePermissionService.findPermissionsOfRole(role.getId())
                        .stream()
                        .map(permissionService::toResponse)
                        .toList()
        );
    }
}
