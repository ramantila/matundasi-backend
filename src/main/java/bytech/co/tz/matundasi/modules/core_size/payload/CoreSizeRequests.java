package bytech.co.tz.matundasi.modules.core_size.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CoreSizeRequests(
        @NotNull(message = "mws_id is required")
        UUID mwsId,

        @PositiveOrZero(message = "pre_collar_to must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "pre_collar_to must have at most 8 digits and 6 decimals")
        BigDecimal preCollarTo,

        @Size(max = 255)
        String preCollarMethod,

        @NotNull(message = "core_from is required")
        @PositiveOrZero(message = "core_from must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "core_from must have at most 8 digits and 6 decimals")
        BigDecimal coreFrom,

        @NotNull(message = "core_to is required")
        @PositiveOrZero(message = "core_to must be zero or greater")
        @Digits(integer = 8, fraction = 6, message = "core_to must have at most 8 digits and 6 decimals")
        BigDecimal coreTo,

        @NotBlank(message = "core_size is required")
        @Size(max = 255)
        String coreSize

)
{}
