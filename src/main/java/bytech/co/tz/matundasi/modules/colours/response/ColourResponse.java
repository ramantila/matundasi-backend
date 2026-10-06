package bytech.co.tz.matundasi.modules.colours.response;

import java.util.UUID;

public record ColourResponse(
        UUID id,
        String colourCode,
        String colourName
) {}
