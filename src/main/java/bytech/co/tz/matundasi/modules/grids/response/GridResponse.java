package bytech.co.tz.matundasi.modules.grids.response;

import java.math.BigDecimal;
import java.util.UUID;

public record GridResponse(
        UUID id,
        String localName,
        BigDecimal e1o,
        BigDecimal n1o,
        BigDecimal e2o,
        BigDecimal n2o,
        BigDecimal delta,
        BigDecimal e1o2,
        BigDecimal n1o2,
        BigDecimal e2o2,
        BigDecimal n2o2,
        String zone,
        String datum,
        String originalGrid,
        String derivedGrid
) {}
