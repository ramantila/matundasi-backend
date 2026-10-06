package bytech.co.tz.matundasi.modules.rab_collar.response;

import java.math.BigDecimal;
import java.util.UUID;

public record RabCollarResponse(
        UUID id,
        UUID mwsId,
        String hole,
        String type,
        BigDecimal utmE,
        BigDecimal utmN,
        BigDecimal hdepth
) {}
