package bytech.co.tz.matundasi.modules.per_area_hole.response;

import java.util.UUID;

public record PerAreaHoleResponse(
        UUID id,
        String buhDiamond,
        String buhRab,
        String buhRc,
        String kilamoDiamond,
        String kilamoRab,
        String kilamoRc,
        String nyaseRab,
        String nyaseRc,
        String nyaseDdh
) {}
