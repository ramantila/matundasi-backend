package bytech.co.tz.matundasi.modules.core_size.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CoreSizeResponse(
        UUID id,
        UUID mwsId,
        String hole,
        BigDecimal preCollarTo,
        String preCollarMethod,
        BigDecimal coreFrom,
        BigDecimal coreTo,
        String coreSize
) {}
