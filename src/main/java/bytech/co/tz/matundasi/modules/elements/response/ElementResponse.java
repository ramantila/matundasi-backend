package bytech.co.tz.matundasi.modules.elements.response;

import java.util.UUID;

public record ElementResponse(
        UUID id,
        String elementName,
        String symbol
) {}
