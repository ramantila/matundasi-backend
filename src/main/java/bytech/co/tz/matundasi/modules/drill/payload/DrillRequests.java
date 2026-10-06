package bytech.co.tz.matundasi.modules.drill.payload;

import bytech.co.tz.matundasi.modules.drill.enums.MoistureState;
import bytech.co.tz.matundasi.modules.drill.enums.SampleMethod;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record DrillRequests(
        @NotNull(message = "order_number is required")
        @PositiveOrZero(message = "order_number must be zero or greater")
        Integer orderNumber,

        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @NotBlank(message = "sample is required")
        @Size(max = 255)
        String sample,

        @NotNull(message = "from_depth is required")
        @PositiveOrZero(message = "from_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "from_depth must have at most 8 digits and 6 decimals")
        BigDecimal fromDepth,

        @NotNull(message = "to_depth is required")
        @PositiveOrZero(message = "to_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "to_depth must have at most 8 digits and 6 decimals")
        BigDecimal toDepth,

        @Size(max = 255)
        String flag,

        @Size(max = 255)
        String flagId,

        Boolean exclude,

        SampleMethod riffleGrab,

        @DecimalMin(value = "0", message = "recovery_percent must be between 0 and 100")
        @DecimalMax(value = "100", message = "recovery_percent must be between 0 and 100")
        @Digits(integer = 3, fraction = 6, message = "recovery_percent must have at most 6 decimals")
        BigDecimal recoveryPercent,

        MoistureState wetDry,

        @Digits(integer = 8, fraction = 6, message = "au must have at most 8 digits and 6 decimals")
        BigDecimal au,

        @Digits(integer = 8, fraction = 6, message = "au_rp1 must have at most 8 digits and 6 decimals")
        BigDecimal auRp1,

        @Digits(integer = 8, fraction = 6, message = "au_rp2 must have at most 8 digits and 6 decimals")
        BigDecimal auRp2,

        @Digits(integer = 8, fraction = 6, message = "au_rp3 must have at most 8 digits and 6 decimals")
        BigDecimal auRp3,

        @Digits(integer = 8, fraction = 6, message = "au_rp4 must have at most 8 digits and 6 decimals")
        BigDecimal auRp4,

        @Digits(integer = 8, fraction = 6, message = "au2 must have at most 8 digits and 6 decimals")
        BigDecimal au2,

        @Digits(integer = 8, fraction = 6, message = "au2_rp1 must have at most 8 digits and 6 decimals")
        BigDecimal au2Rp1,

        @Digits(integer = 8, fraction = 6, message = "au2_plot must have at most 8 digits and 6 decimals")
        BigDecimal au2Plot,

        String comments,

        @NotNull(message = "element_id is required")
        UUID elementId

)
{}
