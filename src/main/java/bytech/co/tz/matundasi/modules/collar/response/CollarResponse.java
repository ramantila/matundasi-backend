package bytech.co.tz.matundasi.modules.collar.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CollarResponse(
        UUID id,
        UUID mwsId,
        String hole,
        BigDecimal utmE,
        BigDecimal utmN,
        String zone,
        BigDecimal localX,
        BigDecimal localY,
        String localName,
        String map125,
        String map50,
        String tenement,
        String claim,
        String localMapSheet,
        BigDecimal rl,
        BigDecimal azimuth,
        BigDecimal utmAz,
        BigDecimal hdepth,
        LocalDate dateDrilled
) {}
