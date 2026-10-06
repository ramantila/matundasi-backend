package bytech.co.tz.matundasi.modules.mw_s.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MwsRequests(
        @NotBlank(message = "hole is required")
        @Size(max = 255)
        String hole,

        @NotBlank(message = "type is required")
        @Size(max = 255)
        String type,

        @NotNull(message = "hdepth is required")
        @PositiveOrZero(message = "hdepth must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "hdepth must have at most 8 digits and 6 decimals")
        BigDecimal hdepth

)
{}
