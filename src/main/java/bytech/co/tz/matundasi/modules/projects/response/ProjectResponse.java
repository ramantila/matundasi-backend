package bytech.co.tz.matundasi.modules.projects.response;

import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String tenement,
        String costCode
) {}
