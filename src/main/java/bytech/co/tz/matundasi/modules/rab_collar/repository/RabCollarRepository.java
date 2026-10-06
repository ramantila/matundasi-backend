package bytech.co.tz.matundasi.modules.rab_collar.repository;

import bytech.co.tz.matundasi.modules.rab_collar.entity.RabCollar;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RabCollarRepository extends JpaRepository<RabCollar, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<RabCollar> findAllByDeletedAtIsNullOrderByMwsHoleAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<RabCollar> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndDeletedAtIsNull(UUID mwsId);

    boolean existsByMwsIdAndDeletedAtIsNullAndIdNot(UUID mwsId, UUID id);
}
