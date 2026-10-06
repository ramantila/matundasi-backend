package bytech.co.tz.matundasi.modules.drill_survey.response;

import java.math.BigDecimal;
import java.util.UUID;

public record DrillSurveyResponse(
        UUID id,
        UUID mwsId,
        String hole,
        BigDecimal downdepth,
        BigDecimal dip,
        BigDecimal azimuth,
        BigDecimal utmAz
) {}
