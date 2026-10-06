package bytech.co.tz.matundasi.modules.pits.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record PitRequests(
        @NotNull(message = "order_number is required")
        @PositiveOrZero(message = "order_number must be zero or greater")
        Integer orderNumber,

        @NotBlank(message = "pit_number is required")
        @Size(max = 255)
        String pitNumber,

        @Digits(integer = 9, fraction = 6, message = "local_e must have at most 9 digits and 6 decimals")
        BigDecimal localE,

        @Digits(integer = 9, fraction = 6, message = "local_n must have at most 9 digits and 6 decimals")
        BigDecimal localN,

        @Digits(integer = 9, fraction = 6, message = "utm_e must have at most 9 digits and 6 decimals")
        BigDecimal utmE,

        @Digits(integer = 9, fraction = 6, message = "utm_n must have at most 9 digits and 6 decimals")
        BigDecimal utmN,

        @Digits(integer = 7, fraction = 6, message = "rl_m must have at most 7 digits and 6 decimals")
        BigDecimal rlM,

        @NotNull(message = "from_depth is required")
        @PositiveOrZero(message = "from_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "from_depth must have at most 8 digits and 6 decimals")
        BigDecimal fromDepth,

        @NotNull(message = "to_depth is required")
        @PositiveOrZero(message = "to_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "to_depth must have at most 8 digits and 6 decimals")
        BigDecimal toDepth,

        UUID lithologyId,

        @Size(max = 255)
        String fraction,

        UUID sampleId,

        @Digits(integer = 8, fraction = 6, message = "au_avg must have at most 8 digits and 6 decimals")
        BigDecimal auAvg,

        @Digits(integer = 8, fraction = 6, message = "au_ppb must have at most 8 digits and 6 decimals")
        BigDecimal auPpb,

        @Digits(integer = 8, fraction = 6, message = "au_rpt1 must have at most 8 digits and 6 decimals")
        BigDecimal auRpt1,

        @Digits(integer = 8, fraction = 6, message = "au_rpt2 must have at most 8 digits and 6 decimals")
        BigDecimal auRpt2,

        @Size(max = 255)
        String laboratory,

        String comments,

        @Size(max = 255)
        String studyType

)
{}
