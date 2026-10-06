package bytech.co.tz.matundasi.modules.branch.repository;

import bytech.co.tz.matundasi.modules.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    List<Branch> findAllByDeletedAtIsNullOrderByNameAsc();

    Optional<Branch> findByIdAndDeletedAtIsNull(UUID id);

    // Backs DataSeeder.
    Optional<Branch> findFirstByNameIgnoreCaseAndDeletedAtIsNull(String name);
}
