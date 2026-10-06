package bytech.co.tz.matundasi.modules.std_reference.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record StdReferenceRequests(
        @NotBlank(message = "standard is required")
        @Size(max = 255)
        String standard,

        @PositiveOrZero(message = "ref_au must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "ref_au must have at most 8 digits and 6 decimals")
        BigDecimal refAu,

        @Size(max = 255)
        String auUnits,

        @PositiveOrZero(message = "n must be zero or greater")
        Integer n,

        @PositiveOrZero(message = "ref_au_ar_aas must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "ref_au_ar_aas must have at most 8 digits and 6 decimals")
        BigDecimal refAuArAas,

        @Size(max = 255)
        String auArUnits,

        @PositiveOrZero(message = "ref_au_fa_50 must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "ref_au_fa_50 must have at most 8 digits and 6 decimals")
        BigDecimal refAuFa50,

        @Size(max = 255)
        String elementsUnit

)
{}
