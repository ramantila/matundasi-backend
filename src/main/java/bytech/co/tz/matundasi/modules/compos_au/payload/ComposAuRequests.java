package bytech.co.tz.matundasi.modules.compos_au.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record ComposAuRequests(
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

        @Digits(integer = 8, fraction = 6, message = "au must have at most 8 digits and 6 decimals")
        BigDecimal au,

        Boolean exclude,

        @Size(max = 255)
        String flag

)
{}
