package bytech.co.tz.matundasi.modules.difference.repository;

import bytech.co.tz.matundasi.modules.difference.entity.Difference;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DifferenceRepository extends JpaRepository<Difference, UUID> {
    @EntityGraph(attributePaths = {"project", "invoice"})
    List<Difference> findAllByDeletedAtIsNullOrderByOrderNumberAsc();

    @EntityGraph(attributePaths = {"project", "invoice"})
    Optional<Difference> findByIdAndDeletedAtIsNull(UUID id);
}
