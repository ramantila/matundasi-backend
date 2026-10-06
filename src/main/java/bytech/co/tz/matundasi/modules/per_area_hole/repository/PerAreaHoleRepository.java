package bytech.co.tz.matundasi.modules.per_area_hole.repository;

import bytech.co.tz.matundasi.modules.per_area_hole.entity.PerAreaHole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerAreaHoleRepository extends JpaRepository<PerAreaHole, UUID> {
    List<PerAreaHole> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    Optional<PerAreaHole> findByIdAndDeletedAtIsNull(UUID id);
}
