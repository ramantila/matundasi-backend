package bytech.co.tz.matundasi.modules.drill.repository;

import bytech.co.tz.matundasi.modules.drill.entity.Drill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DrillRepository extends JpaRepository<Drill, UUID> {
    @EntityGraph(attributePaths = {"element", "mws"})
    List<Drill> findAllByDeletedAtIsNullOrderByMwsHoleAscOrderNumberAsc();

    @EntityGraph(attributePaths = {"element", "mws"})
    Optional<Drill> findByIdAndDeletedAtIsNull(UUID id);
}
