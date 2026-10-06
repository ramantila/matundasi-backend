package bytech.co.tz.matundasi.modules.schs.repository;

import bytech.co.tz.matundasi.modules.schs.entity.Schistosity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchistosityRepository extends JpaRepository<Schistosity, UUID> {
    List<Schistosity> findAllByDeletedAtIsNullOrderByCodeAsc();

    Optional<Schistosity> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);
}
