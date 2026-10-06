package bytech.co.tz.matundasi.modules.collar.payload;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CollarRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @Digits(integer = 9, fraction = 6, message = "utm_e must have at most 9 digits and 6 decimals")
        BigDecimal utmE,

        @Digits(integer = 9, fraction = 6, message = "utm_n must have at most 9 digits and 6 decimals")
        BigDecimal utmN,

        @Size(max = 255)
        String zone,

        @Digits(integer = 9, fraction = 6, message = "local_x must have at most 9 digits and 6 decimals")
        BigDecimal localX,

        @Digits(integer = 9, fraction = 6, message = "local_y must have at most 9 digits and 6 decimals")
        BigDecimal localY,

        @Size(max = 255)
        String localName,

        @Size(max = 255)
        String map125,

        @Size(max = 255)
        String map50,

        @Size(max = 255)
        String tenement,

        @Size(max = 255)
        String claim,

        @Size(max = 255)
        String localMapSheet,

        @Digits(integer = 7, fraction = 6, message = "rl must have at most 7 digits and 6 decimals")
        BigDecimal rl,

        @DecimalMin(value = "0", message = "azimuth must be between 0 and 360")
        @DecimalMax(value = "360", message = "azimuth must be between 0 and 360")
        @Digits(integer = 3, fraction = 6, message = "azimuth must have at most 6 decimals")
        BigDecimal azimuth,

        @DecimalMin(value = "0", message = "utm_az must be between 0 and 360")
        @DecimalMax(value = "360", message = "utm_az must be between 0 and 360")
        @Digits(integer = 3, fraction = 6, message = "utm_az must have at most 6 decimals")
        BigDecimal utmAz,

        @PositiveOrZero(message = "hdepth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "hdepth must have at most 8 digits and 6 decimals")
        BigDecimal hdepth,

        @PastOrPresent(message = "date_drilled cannot be in the future")
        LocalDate dateDrilled

)
{}
