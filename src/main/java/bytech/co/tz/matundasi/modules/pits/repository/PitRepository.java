package bytech.co.tz.matundasi.modules.pits.repository;

import bytech.co.tz.matundasi.modules.pits.entity.Pit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PitRepository extends JpaRepository<Pit, UUID> {
    @EntityGraph(attributePaths = {"lithology", "lithology.weathcode", "sample", "sample.element"})
    List<Pit> findAllByDeletedAtIsNullOrderByPitNumberAscOrderNumberAsc();

    @EntityGraph(attributePaths = {"lithology", "lithology.weathcode", "sample", "sample.element"})
    Optional<Pit> findByIdAndDeletedAtIsNull(UUID id);
}
