package bytech.co.tz.matundasi.modules.lith.repository;

import bytech.co.tz.matundasi.modules.lith.entity.Lithology;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LithologyRepository extends JpaRepository<Lithology, UUID> {
    @EntityGraph(attributePaths = {"weathcode", "mws"})
    List<Lithology> findAllByDeletedAtIsNullOrderByMwsHoleAscLithfromAsc();

    @EntityGraph(attributePaths = {"weathcode", "mws"})
    Optional<Lithology> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndLithfromAndDeletedAtIsNull(UUID mwsId, BigDecimal lithfrom);

    boolean existsByMwsIdAndLithfromAndDeletedAtIsNullAndIdNot(UUID mwsId, BigDecimal lithfrom, UUID id);
}
