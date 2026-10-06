package bytech.co.tz.matundasi.modules.sizes.response;

import java.util.UUID;

public record SizeResponse(
        UUID id,
        String code,
        String grain
) {}
