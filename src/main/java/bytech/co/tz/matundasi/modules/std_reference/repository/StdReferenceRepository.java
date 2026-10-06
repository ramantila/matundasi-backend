package bytech.co.tz.matundasi.modules.std_reference.repository;

import bytech.co.tz.matundasi.modules.std_reference.entity.StdReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StdReferenceRepository extends JpaRepository<StdReference, UUID> {
    List<StdReference> findAllByDeletedAtIsNullOrderByStandardAsc();

    Optional<StdReference> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByStandardIgnoreCaseAndDeletedAtIsNull(String standard);

    boolean existsByStandardIgnoreCaseAndDeletedAtIsNullAndIdNot(String standard, UUID id);
}
