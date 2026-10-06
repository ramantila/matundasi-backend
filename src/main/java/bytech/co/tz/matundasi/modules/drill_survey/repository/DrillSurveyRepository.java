package bytech.co.tz.matundasi.modules.drill_survey.repository;

import bytech.co.tz.matundasi.modules.drill_survey.entity.DrillSurvey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DrillSurveyRepository extends JpaRepository<DrillSurvey, UUID> {
    @EntityGraph(attributePaths = "mws")
    List<DrillSurvey> findAllByDeletedAtIsNullOrderByMwsHoleAscDowndepthAsc();

    @EntityGraph(attributePaths = "mws")
    Optional<DrillSurvey> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMwsIdAndDowndepthAndDeletedAtIsNull(UUID mwsId, BigDecimal downdepth);

    boolean existsByMwsIdAndDowndepthAndDeletedAtIsNullAndIdNot(UUID mwsId, BigDecimal downdepth, UUID id);
}
