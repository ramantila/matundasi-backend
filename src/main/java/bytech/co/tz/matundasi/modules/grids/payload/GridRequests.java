package bytech.co.tz.matundasi.modules.grids.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record GridRequests(
        @NotBlank(message = "local_name is required")
        @Size(max = 255)
        String localName,

        @Digits(integer = 10, fraction = 6, message = "e1o must have at most 10 digits and 6 decimals")
        BigDecimal e1o,

        @Digits(integer = 10, fraction = 6, message = "n1o must have at most 10 digits and 6 decimals")
        BigDecimal n1o,

        @Digits(integer = 10, fraction = 6, message = "e2o must have at most 10 digits and 6 decimals")
        BigDecimal e2o,

        @Digits(integer = 10, fraction = 6, message = "n2o must have at most 10 digits and 6 decimals")
        BigDecimal n2o,

        @Digits(integer = 8, fraction = 6, message = "delta must have at most 8 digits and 6 decimals")
        BigDecimal delta,

        @Digits(integer = 10, fraction = 6, message = "e1o2 must have at most 10 digits and 6 decimals")
        BigDecimal e1o2,

        @Digits(integer = 10, fraction = 6, message = "n1o2 must have at most 10 digits and 6 decimals")
        BigDecimal n1o2,

        @Digits(integer = 10, fraction = 6, message = "e2o2 must have at most 10 digits and 6 decimals")
        BigDecimal e2o2,

        @Digits(integer = 10, fraction = 6, message = "n2o2 must have at most 10 digits and 6 decimals")
        BigDecimal n2o2,

        @Size(max = 255)
        String zone,

        @Size(max = 255)
        String datum,

        @Size(max = 255)
        String originalGrid,

        @Size(max = 255)
        String derivedGrid

)
{}
