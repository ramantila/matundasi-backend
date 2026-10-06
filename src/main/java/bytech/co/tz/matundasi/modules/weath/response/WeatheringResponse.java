package bytech.co.tz.matundasi.modules.weath.response;

import java.util.UUID;

public record WeatheringResponse(
        UUID id,
        String code,
        String description
) {}
