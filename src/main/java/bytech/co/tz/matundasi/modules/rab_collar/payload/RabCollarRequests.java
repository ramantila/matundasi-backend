package bytech.co.tz.matundasi.modules.rab_collar.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record RabCollarRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @Size(max = 255)
        String type,

        @Digits(integer = 9, fraction = 6, message = "utm_e must have at most 9 digits and 6 decimals")
        BigDecimal utmE,

        @Digits(integer = 9, fraction = 6, message = "utm_n must have at most 9 digits and 6 decimals")
        BigDecimal utmN,

        @PositiveOrZero(message = "hdepth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "hdepth must have at most 8 digits and 6 decimals")
        BigDecimal hdepth

)
{}
