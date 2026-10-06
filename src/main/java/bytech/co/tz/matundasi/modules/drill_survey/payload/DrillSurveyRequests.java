package bytech.co.tz.matundasi.modules.drill_survey.payload;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record DrillSurveyRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @NotNull(message = "downdepth is required")
        @PositiveOrZero(message = "downdepth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "downdepth must have at most 8 digits and 6 decimals")
        BigDecimal downdepth,

        @NotNull(message = "dip is required")
        @DecimalMin(value = "-90", message = "dip must be between -90 and 90")
        @DecimalMax(value = "90", message = "dip must be between -90 and 90")
        @Digits(integer = 3, fraction = 6, message = "dip must have at most 6 decimals")
        BigDecimal dip,

        @NotNull(message = "azimuth is required")
        @DecimalMin(value = "0", message = "azimuth must be between 0 and 360")
        @DecimalMax(value = "360", message = "azimuth must be between 0 and 360")
        @Digits(integer = 3, fraction = 6, message = "azimuth must have at most 6 decimals")
        BigDecimal azimuth,

        @NotNull(message = "utm_az is required")
        @DecimalMin(value = "0", message = "utm_az must be between 0 and 360")
        @DecimalMax(value = "360", message = "utm_az must be between 0 and 360")
        @Digits(integer = 3, fraction = 6, message = "utm_az must have at most 6 decimals")
        BigDecimal utmAz

)
{}
