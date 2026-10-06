package bytech.co.tz.matundasi.modules.sizes.repository;

import bytech.co.tz.matundasi.modules.sizes.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SizeRepository extends JpaRepository<Size, UUID> {
    List<Size> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    Optional<Size> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);
}
