package bytech.co.tz.matundasi.modules.lith.payload;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record LithologyRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @NotNull(message = "lithfrom is required")
        @PositiveOrZero(message = "lithfrom must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "lithfrom must have at most 8 digits and 6 decimals")
        BigDecimal lithfrom,

        @NotNull(message = "lithto is required")
        @PositiveOrZero(message = "lithto must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "lithto must have at most 8 digits and 6 decimals")
        BigDecimal lithto,

        @NotBlank(message = "lithcode is required")
        @Size(max = 255)
        String lithcode,

        @DecimalMin(value = "0", message = "quartz_percent must be between 0 and 100")
        @DecimalMax(value = "100", message = "quartz_percent must be between 0 and 100")
        @Digits(integer = 3, fraction = 6, message = "quartz_percent must have at most 6 decimals")
        BigDecimal quartzPercent,

        @DecimalMin(value = "0", message = "pyrites_percent must be between 0 and 100")
        @DecimalMax(value = "100", message = "pyrites_percent must be between 0 and 100")
        @Digits(integer = 3, fraction = 6, message = "pyrites_percent must have at most 6 decimals")
        BigDecimal pyritesPercent,

        @Size(max = 255)
        String alteration,

        @Size(max = 255)
        String schistosity,

        @Size(max = 255)
        String grainsize,

        @Size(max = 255)
        String colour,

        UUID weathcodeId,

        String comments

)
{}
