package bytech.co.tz.matundasi.modules.roles.repository;

import bytech.co.tz.matundasi.modules.roles.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAllByDeletedAtIsNullOrderByNameAsc();

    List<Role> findAllByDeletedAtIsNullAndActiveTrueOrderByNameAsc();

    Optional<Role> findByIdAndDeletedAtIsNull(UUID id);

    // Backs DataSeeder.
    Optional<Role> findFirstByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String name, UUID id);
}
