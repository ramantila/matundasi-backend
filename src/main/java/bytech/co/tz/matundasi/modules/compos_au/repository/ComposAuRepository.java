package bytech.co.tz.matundasi.modules.compos_au.repository;

import bytech.co.tz.matundasi.modules.compos_au.entity.ComposAu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComposAuRepository extends JpaRepository<ComposAu, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<ComposAu> findAllByDeletedAtIsNullOrderByMwsHoleAscFromDepthAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<ComposAu> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndFromDepthAndDeletedAtIsNull(UUID mwsId, BigDecimal fromDepth);

    boolean existsByMwsIdAndFromDepthAndDeletedAtIsNullAndIdNot(UUID mwsId, BigDecimal fromDepth, UUID id);
}
