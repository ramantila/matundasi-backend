package bytech.co.tz.matundasi.modules.sample.response;

import java.math.BigDecimal;
import java.util.UUID;

public record SampleResponse(
        UUID id,
        Integer orderNumber,
        String sample,
        String flag,
        String flagId,
        BigDecimal utmE,
        BigDecimal utmN,
        String zone,
        BigDecimal localX,
        BigDecimal localY,
        String localName,
        String tenement,
        String map125,
        String map50,
        String code,
        String description,
        BigDecimal au,
        BigDecimal auRp1,
        BigDecimal auRp2,
        BigDecimal au2,
        UUID elementId,
        String comments,
        String claim
) {}
