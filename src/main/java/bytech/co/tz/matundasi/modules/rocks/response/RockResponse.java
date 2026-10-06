package bytech.co.tz.matundasi.modules.rocks.response;

import java.util.UUID;

public record RockResponse(
        UUID id,
        String rockCode,
        String description,
        String regime
) {}
