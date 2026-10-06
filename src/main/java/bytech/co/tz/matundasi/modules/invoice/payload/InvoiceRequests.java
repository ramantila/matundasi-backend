package bytech.co.tz.matundasi.modules.invoice.payload;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceRequests(
        @NotNull(message = "order_number is required")
        @PositiveOrZero(message = "order_number must be zero or greater")
        Integer orderNumber,

        @NotBlank(message = "invoice_number is required")
        @Size(max = 255)
        String invoiceNumber,

        @Size(max = 255)
        String labName,

        @Size(max = 255)
        String analysis,

        @Size(max = 255)
        String jobNumber,

        @PositiveOrZero(message = "amount_in_us must be zero or greater")
        @Digits(integer = 12, fraction = 6, message = "amount_in_us must have at most 12 digits and 6 decimals")
        BigDecimal amountInUs,

        @Size(max = 255)
        String accountCode,

        LocalDate invoiceDate,

        LocalDate dateApproved

)
{}
