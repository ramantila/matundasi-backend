package bytech.co.tz.matundasi.modules.rolepermissions.service;

import bytech.co.tz.matundasi.modules.permissions.entity.Permission;
import bytech.co.tz.matundasi.modules.permissions.repository.PermissionRepository;
import bytech.co.tz.matundasi.modules.rolepermissions.entity.RolePermission;
import bytech.co.tz.matundasi.modules.rolepermissions.payload.RolePermissionRequest;
import bytech.co.tz.matundasi.modules.rolepermissions.repository.RolePermissionRepository;
import bytech.co.tz.matundasi.modules.rolepermissions.response.RolePermissionResponse;
import bytech.co.tz.matundasi.modules.roles.entity.Role;
import bytech.co.tz.matundasi.modules.roles.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public RolePermissionResponse create(RolePermissionRequest request) {
        Role role = getActiveRole(request.roleId());
        Permission permission = getActivePermission(request.permissionId());

        if (rolePermissionRepository.existsByRole_IdAndPermission_IdAndDeletedAtIsNull(role.getId(), permission.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This role already has this permission");
        }

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        return toResponse(rolePermissionRepository.save(rolePermission));
    }

    @Transactional(readOnly = true)
    public List<RolePermissionResponse> findAll(UUID roleId) {
        List<RolePermission> rolePermissions = roleId == null
                ? rolePermissionRepository.findAllByDeletedAtIsNullAndRole_DeletedAtIsNullAndPermission_DeletedAtIsNull()
                : rolePermissionRepository.findAllByRole_IdAndDeletedAtIsNullAndPermission_DeletedAtIsNull(roleId);
        return rolePermissions.stream()
                .sorted(Comparator.comparing((RolePermission rp) -> rp.getRole().getName())
                        .thenComparing(rp -> rp.getPermission().getName()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RolePermissionResponse findById(UUID id) {
        return toResponse(getActiveRolePermission(id));
    }

    @Transactional
    public void delete(UUID id) {
        RolePermission rolePermission = getActiveRolePermission(id);
        rolePermission.softDelete();
        rolePermissionRepository.save(rolePermission);
    }

    // Active permissions currently granted to a role, sorted by name.
    @Transactional(readOnly = true)
    public List<Permission> findPermissionsOfRole(UUID roleId) {
        return rolePermissionRepository.findAllByRole_IdAndDeletedAtIsNullAndPermission_DeletedAtIsNull(roleId)
                .stream()
                .map(RolePermission::getPermission)
                .sorted(Comparator.comparing(Permission::getName))
                .toList();
    }

    // Makes the role's permissions exactly permissionIds: adds missing ones
    // and soft-deletes the rest. Used by RoleService on create and update.
    @Transactional
    public void replacePermissions(Role role, Set<UUID> permissionIds) {
        Set<UUID> wanted = permissionIds == null ? Set.of() : permissionIds;
        List<Permission> permissions = wanted.isEmpty()
                ? List.of()
                : permissionRepository.findAllByIdInAndDeletedAtIsNull(wanted);
        if (permissions.size() != wanted.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more permission ids are invalid");
        }

        Set<UUID> existing = new HashSet<>();
        for (RolePermission rolePermission : rolePermissionRepository.findAllByRole_IdAndDeletedAtIsNull(role.getId())) {
            UUID permissionId = rolePermission.getPermission().getId();
            if (wanted.contains(permissionId) && existing.add(permissionId)) {
                continue;
            }
            rolePermission.softDelete();
            rolePermissionRepository.save(rolePermission);
        }

        for (Permission permission : permissions) {
            if (!existing.contains(permission.getId())) {
                rolePermissionRepository.save(RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build());
            }
        }
    }

    @Transactional
    public void removeAllForRole(UUID roleId) {
        for (RolePermission rolePermission : rolePermissionRepository.findAllByRole_IdAndDeletedAtIsNull(roleId)) {
            rolePermission.softDelete();
            rolePermissionRepository.save(rolePermission);
        }
    }

    private RolePermission getActiveRolePermission(UUID id) {
        return rolePermissionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role permission not found"));
    }

    private Role getActiveRole(UUID roleId) {
        return roleRepository.findByIdAndDeletedAtIsNull(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    private Permission getActivePermission(UUID permissionId) {
        return permissionRepository.findByIdAndDeletedAtIsNull(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found"));
    }

    private RolePermissionResponse toResponse(RolePermission rolePermission) {
        return new RolePermissionResponse(
                rolePermission.getId(),
                rolePermission.getRole().getId(),
                rolePermission.getRole().getName(),
                rolePermission.getPermission().getId(),
                rolePermission.getPermission().getName()
        );
    }
}
