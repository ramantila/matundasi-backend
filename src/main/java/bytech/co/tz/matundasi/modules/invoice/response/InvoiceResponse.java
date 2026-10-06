package bytech.co.tz.matundasi.modules.invoice.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        Integer orderNumber,
        String invoiceNumber,
        String labName,
        String analysis,
        String jobNumber,
        BigDecimal amountInUs,
        String accountCode,
        LocalDate invoiceDate,
        LocalDate dateApproved
) {}
