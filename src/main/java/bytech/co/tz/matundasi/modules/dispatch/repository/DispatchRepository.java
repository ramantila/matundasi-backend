package bytech.co.tz.matundasi.modules.dispatch.repository;

import bytech.co.tz.matundasi.modules.dispatch.entity.Dispatch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, UUID> {
    @EntityGraph(attributePaths = {"project", "invoice"})
    List<Dispatch> findAllByDeletedAtIsNullOrderByOrderNumberAsc();

    @EntityGraph(attributePaths = {"project", "invoice"})
    Optional<Dispatch> findByIdAndDeletedAtIsNull(UUID id);
}
