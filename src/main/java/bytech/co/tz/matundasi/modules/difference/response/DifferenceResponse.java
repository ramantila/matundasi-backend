package bytech.co.tz.matundasi.modules.difference.response;

import java.time.LocalDate;
import java.util.UUID;

public record DifferenceResponse(
        UUID id,
        Integer orderNumber,
        String sampleNoRange,
        UUID projectId,
        String preLab,
        String lab,
        String labLoc,
        String batch,
        LocalDate dispatchDate,
        Integer numberOfSamples,
        Integer numberAssayed,
        LocalDate dateReceived,
        UUID invoiceId,
        String type
) {}
