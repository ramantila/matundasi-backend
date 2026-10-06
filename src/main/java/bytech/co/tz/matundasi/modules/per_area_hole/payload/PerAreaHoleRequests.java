package bytech.co.tz.matundasi.modules.per_area_hole.payload;

import jakarta.validation.constraints.Size;

public record PerAreaHoleRequests(
        @Size(max = 255)
        String buhDiamond,

        @Size(max = 255)
        String buhRab,

        @Size(max = 255)
        String buhRc,

        @Size(max = 255)
        String kilamoDiamond,

        @Size(max = 255)
        String kilamoRab,

        @Size(max = 255)
        String kilamoRc,

        @Size(max = 255)
        String nyaseRab,

        @Size(max = 255)
        String nyaseRc,

        @Size(max = 255)
        String nyaseDdh

)
{}
