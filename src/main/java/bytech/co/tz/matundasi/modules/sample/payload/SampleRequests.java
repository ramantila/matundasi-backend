package bytech.co.tz.matundasi.modules.sample.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record SampleRequests(
        @NotNull(message = "order_number is required")
        @PositiveOrZero(message = "order_number must be zero or greater")
        Integer orderNumber,

        @NotBlank(message = "sample is required")
        @Size(max = 255)
        String sample,

        @Size(max = 255)
        String flag,

        @Size(max = 255)
        String flagId,

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
        String tenement,

        @Size(max = 255)
        String map125,

        @Size(max = 255)
        String map50,

        @Size(max = 255)
        String code,

        String description,

        @Digits(integer = 8, fraction = 6, message = "au must have at most 8 digits and 6 decimals")
        BigDecimal au,

        @Digits(integer = 8, fraction = 6, message = "au_rp1 must have at most 8 digits and 6 decimals")
        BigDecimal auRp1,

        @Digits(integer = 8, fraction = 6, message = "au_rp2 must have at most 8 digits and 6 decimals")
        BigDecimal auRp2,

        @Digits(integer = 8, fraction = 6, message = "au2 must have at most 8 digits and 6 decimals")
        BigDecimal au2,

        UUID elementId,

        String comments,

        @Size(max = 255)
        String claim

)
{}
