package bytech.co.tz.matundasi.modules.rolepermissions.repository;

import bytech.co.tz.matundasi.modules.rolepermissions.entity.RolePermission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    @EntityGraph(attributePaths = {"role", "permission"})
    List<RolePermission> findAllByDeletedAtIsNullAndRole_DeletedAtIsNullAndPermission_DeletedAtIsNull();

    @EntityGraph(attributePaths = {"role", "permission"})
    List<RolePermission> findAllByRole_IdAndDeletedAtIsNullAndPermission_DeletedAtIsNull(UUID roleId);

    @EntityGraph(attributePaths = {"role", "permission"})
    Optional<RolePermission> findByIdAndDeletedAtIsNull(UUID id);

    // Backs AuthService#login: every active permission granted by any of the user's roles.
    @EntityGraph(attributePaths = "permission")
    List<RolePermission> findAllByRole_IdInAndDeletedAtIsNullAndPermission_DeletedAtIsNull(Collection<UUID> roleIds);

    // Includes rows whose permission was soft-deleted, so RolePermissionService
    // can tidy them up when a role's permissions are replaced or the role is deleted.
    List<RolePermission> findAllByRole_IdAndDeletedAtIsNull(UUID roleId);

    boolean existsByRole_IdAndPermission_IdAndDeletedAtIsNull(UUID roleId, UUID permissionId);
}
