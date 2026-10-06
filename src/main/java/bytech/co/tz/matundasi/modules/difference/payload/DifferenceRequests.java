package bytech.co.tz.matundasi.modules.difference.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record DifferenceRequests(
        @NotNull(message = "order_number is required")
        @PositiveOrZero(message = "order_number must be zero or greater")
        Integer orderNumber,

        @Size(max = 255)
        String sampleNoRange,

        UUID projectId,

        @Size(max = 255)
        String preLab,

        @Size(max = 255)
        String lab,

        @Size(max = 255)
        String labLoc,

        @Size(max = 255)
        String batch,

        LocalDate dispatchDate,

        @PositiveOrZero(message = "number_of_samples must be zero or greater")
        Integer numberOfSamples,

        @PositiveOrZero(message = "number_assayed must be zero or greater")
        Integer numberAssayed,

        LocalDate dateReceived,

        UUID invoiceId,

        @Size(max = 255)
        String type

)
{}
