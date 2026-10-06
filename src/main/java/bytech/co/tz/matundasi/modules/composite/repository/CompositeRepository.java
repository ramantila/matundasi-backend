package bytech.co.tz.matundasi.modules.composite.repository;

import bytech.co.tz.matundasi.modules.composite.entity.Composite;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompositeRepository extends JpaRepository<Composite, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<Composite> findAllByDeletedAtIsNullOrderByMwsHoleAscFromDepthAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<Composite> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndFromDepthAndDeletedAtIsNull(UUID mwsId, BigDecimal fromDepth);

    boolean existsByMwsIdAndFromDepthAndDeletedAtIsNullAndIdNot(UUID mwsId, BigDecimal fromDepth, UUID id);
}
