package bytech.co.tz.matundasi.modules.permissions.repository;

import bytech.co.tz.matundasi.modules.permissions.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    List<Permission> findAllByDeletedAtIsNullOrderByNameAsc();

    Optional<Permission> findByIdAndDeletedAtIsNull(UUID id);

    List<Permission> findAllByIdInAndDeletedAtIsNull(Collection<UUID> ids);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String name, UUID id);
}
