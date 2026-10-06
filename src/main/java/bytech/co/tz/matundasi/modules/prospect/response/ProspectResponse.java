package bytech.co.tz.matundasi.modules.prospect.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProspectResponse(
        UUID id,
        String name,
        String localName,
        BigDecimal minX,
        BigDecimal minY,
        BigDecimal maxX,
        BigDecimal maxY
) {}
