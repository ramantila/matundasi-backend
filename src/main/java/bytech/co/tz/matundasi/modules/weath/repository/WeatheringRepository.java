package bytech.co.tz.matundasi.modules.weath.repository;

import bytech.co.tz.matundasi.modules.weath.entity.Weathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeatheringRepository extends JpaRepository<Weathering, UUID> {
    List<Weathering> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    Optional<Weathering> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNull(String code);

    boolean existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String code, UUID id);
}
