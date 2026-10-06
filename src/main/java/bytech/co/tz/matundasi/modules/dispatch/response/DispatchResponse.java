package bytech.co.tz.matundasi.modules.dispatch.response;

import java.time.LocalDate;
import java.util.UUID;

public record DispatchResponse(
        UUID id,
        Integer orderNumber,
        String sampleNoRange,
        String originator,
        String gridHoles,
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
