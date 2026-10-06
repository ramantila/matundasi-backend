package bytech.co.tz.matundasi.modules.rocks.repository;

import bytech.co.tz.matundasi.modules.rocks.entity.Rock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RockRepository extends JpaRepository<Rock, UUID> {
    List<Rock> findAllByDeletedAtIsNullOrderByRockCodeAsc();

    Optional<Rock> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByRockCodeIgnoreCaseAndDeletedAtIsNull(String rockCode);

    boolean existsByRockCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String rockCode, UUID id);
}
