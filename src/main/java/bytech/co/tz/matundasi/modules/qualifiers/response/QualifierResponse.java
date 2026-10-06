package bytech.co.tz.matundasi.modules.qualifiers.response;

import java.util.UUID;

public record QualifierResponse(
        UUID id,
        String qualifierCode,
        String description
) {}
