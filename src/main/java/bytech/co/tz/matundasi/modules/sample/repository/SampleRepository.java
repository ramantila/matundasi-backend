package bytech.co.tz.matundasi.modules.sample.repository;

import bytech.co.tz.matundasi.modules.sample.entity.Sample;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleRepository extends JpaRepository<Sample, UUID> {
    @EntityGraph(attributePaths = "element")
    List<Sample> findAllByDeletedAtIsNullOrderByOrderNumberAsc();

    @EntityGraph(attributePaths = "element")
    Optional<Sample> findByIdAndDeletedAtIsNull(UUID id);
}
