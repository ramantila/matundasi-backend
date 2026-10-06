package bytech.co.tz.matundasi.modules.core_size.repository;

import bytech.co.tz.matundasi.modules.core_size.entity.CoreSize;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoreSizeRepository extends JpaRepository<CoreSize, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<CoreSize> findAllByDeletedAtIsNullOrderByMwsHoleAscCoreFromAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<CoreSize> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndCoreFromAndDeletedAtIsNull(UUID mwsId, BigDecimal coreFrom);

    boolean existsByMwsIdAndCoreFromAndDeletedAtIsNullAndIdNot(UUID mwsId, BigDecimal coreFrom, UUID id);
}
