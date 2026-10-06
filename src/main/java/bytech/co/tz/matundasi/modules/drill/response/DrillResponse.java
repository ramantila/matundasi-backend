package bytech.co.tz.matundasi.modules.drill.response;

import bytech.co.tz.matundasi.modules.drill.enums.MoistureState;
import bytech.co.tz.matundasi.modules.drill.enums.SampleMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record DrillResponse(
        UUID id,
        Integer orderNumber,
        UUID mwsId,
        String hole,
        String sample,
        BigDecimal fromDepth,
        BigDecimal toDepth,
        String flag,
        String flagId,
        Boolean exclude,
        SampleMethod riffleGrab,
        BigDecimal recoveryPercent,
        MoistureState wetDry,
        BigDecimal au,
        BigDecimal auRp1,
        BigDecimal auRp2,
        BigDecimal auRp3,
        BigDecimal auRp4,
        BigDecimal au2,
        BigDecimal au2Rp1,
        BigDecimal au2Plot,
        String comments,
        UUID elementId
) {}
