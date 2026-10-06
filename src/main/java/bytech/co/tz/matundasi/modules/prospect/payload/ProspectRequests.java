package bytech.co.tz.matundasi.modules.prospect.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProspectRequests(
        @NotBlank(message = "name is required")
        @Size(max = 255)
        String name,

        @Size(max = 255)
        String localName,

        @Digits(integer = 9, fraction = 6, message = "min_x must have at most 9 digits and 6 decimals")
        BigDecimal minX,

        @Digits(integer = 9, fraction = 6, message = "min_y must have at most 9 digits and 6 decimals")
        BigDecimal minY,

        @Digits(integer = 9, fraction = 6, message = "max_x must have at most 9 digits and 6 decimals")
        BigDecimal maxX,

        @Digits(integer = 9, fraction = 6, message = "max_y must have at most 9 digits and 6 decimals")
        BigDecimal maxY

)
{}
