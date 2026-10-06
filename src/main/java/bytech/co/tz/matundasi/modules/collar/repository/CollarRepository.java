package bytech.co.tz.matundasi.modules.collar.repository;

import bytech.co.tz.matundasi.modules.collar.entity.Collar;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollarRepository extends JpaRepository<Collar, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<Collar> findAllByDeletedAtIsNullOrderByMwsHoleAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<Collar> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndDeletedAtIsNull(UUID mwsId);

    boolean existsByMwsIdAndDeletedAtIsNullAndIdNot(UUID mwsId, UUID id);
}
