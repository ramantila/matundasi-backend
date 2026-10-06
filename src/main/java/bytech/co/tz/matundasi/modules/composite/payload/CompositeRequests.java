package bytech.co.tz.matundasi.modules.composite.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CompositeRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @NotNull(message = "from_depth is required")
        @PositiveOrZero(message = "from_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "from_depth must have at most 8 digits and 6 decimals")
        BigDecimal fromDepth,

        @NotNull(message = "to_depth is required")
        @PositiveOrZero(message = "to_depth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "to_depth must have at most 8 digits and 6 decimals")
        BigDecimal toDepth,

        @PositiveOrZero(message = "average_ni must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "average_ni must have at most 8 digits and 6 decimals")
        BigDecimal averageNi,

        @PositiveOrZero(message = "average_co must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "average_co must have at most 8 digits and 6 decimals")
        BigDecimal averageCo

)
{}
