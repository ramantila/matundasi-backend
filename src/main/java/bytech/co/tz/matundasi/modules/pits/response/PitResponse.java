package bytech.co.tz.matundasi.modules.pits.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PitResponse(
        UUID id,
        Integer orderNumber,
        String pitNumber,
        BigDecimal localE,
        BigDecimal localN,
        BigDecimal utmE,
        BigDecimal utmN,
        BigDecimal rlM,
        BigDecimal fromDepth,
        BigDecimal toDepth,
        UUID lithologyId,
        String fraction,
        UUID sampleId,
        BigDecimal auAvg,
        BigDecimal auPpb,
        BigDecimal auRpt1,
        BigDecimal auRpt2,
        String laboratory,
        String comments,
        String studyType
) {}
