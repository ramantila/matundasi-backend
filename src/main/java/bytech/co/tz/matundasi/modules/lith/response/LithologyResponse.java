package bytech.co.tz.matundasi.modules.lith.response;

import java.math.BigDecimal;
import java.util.UUID;

public record LithologyResponse(
        UUID id,
        UUID mwsId,
        String hole,
        BigDecimal lithfrom,
        BigDecimal lithto,
        String lithcode,
        BigDecimal quartzPercent,
        BigDecimal pyritesPercent,
        String alteration,
        String schistosity,
        String grainsize,
        String colour,
        UUID weathcodeId,
        String comments
) {}
