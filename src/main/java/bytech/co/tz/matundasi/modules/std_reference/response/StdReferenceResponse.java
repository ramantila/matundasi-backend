package bytech.co.tz.matundasi.modules.std_reference.response;

import java.math.BigDecimal;
import java.util.UUID;

public record StdReferenceResponse(
        UUID id,
        String standard,
        BigDecimal refAu,
        String auUnits,
        Integer n,
        BigDecimal refAuArAas,
        String auArUnits,
        BigDecimal refAuFa50,
        String elementsUnit
) {}
