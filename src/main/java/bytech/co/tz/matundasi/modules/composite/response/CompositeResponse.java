package bytech.co.tz.matundasi.modules.composite.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CompositeResponse(
        UUID id,
        UUID mwsId,
        String hole,
        BigDecimal fromDepth,
        BigDecimal toDepth,
        BigDecimal length,
        BigDecimal averageNi,
        BigDecimal averageCo
) {}
