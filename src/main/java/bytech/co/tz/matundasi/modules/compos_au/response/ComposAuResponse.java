package bytech.co.tz.matundasi.modules.compos_au.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ComposAuResponse(
        UUID id,
        UUID mwsId,
        String hole,
        String sample,
        BigDecimal fromDepth,
        BigDecimal toDepth,
        BigDecimal length,
        BigDecimal au,
        Boolean exclude,
        String flag
) {}
