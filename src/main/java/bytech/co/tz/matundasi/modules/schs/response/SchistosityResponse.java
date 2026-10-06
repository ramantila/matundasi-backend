package bytech.co.tz.matundasi.modules.schs.response;

import java.util.UUID;

public record SchistosityResponse(
        UUID id,
        String code,
        String schistosity
) {}
