package bytech.co.tz.matundasi.modules.mw_s.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MwsResponse(
        UUID id,
        String hole,
        String type,
        BigDecimal hdepth
) {}
