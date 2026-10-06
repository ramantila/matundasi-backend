package bytech.co.tz.matundasi.modules.permissions.service;

import bytech.co.tz.matundasi.modules.permissions.entity.Permission;
import bytech.co.tz.matundasi.modules.permissions.payload.PermissionRequest;
import bytech.co.tz.matundasi.modules.permissions.repository.PermissionRepository;
import bytech.co.tz.matundasi.modules.permissions.response.PermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Transactional
    public PermissionResponse create(PermissionRequest request) {
        String name = request.name().trim();

        if (permissionRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission name already exists");
        }

        Permission permission = Permission.builder()
                .name(name)
                .build();

        return toResponse(permissionRepository.save(permission));
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PermissionResponse findById(UUID id) {
        return toResponse(getActivePermission(id));
    }

    @Transactional
    public PermissionResponse update(UUID id, PermissionRequest request) {
        Permission permission = getActivePermission(id);
        String name = request.name().trim();

        if (permissionRepository.existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(name, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission name already exists");
        }

        permission.setName(name);

        return toResponse(permissionRepository.save(permission));
    }

    @Transactional
    public void delete(UUID id) {
        Permission permission = getActivePermission(id);
        permission.softDelete();
        permissionRepository.save(permission);
    }

    private Permission getActivePermission(UUID id) {
        return permissionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found"));
    }

    public PermissionResponse toResponse(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName()
        );
    }
}
