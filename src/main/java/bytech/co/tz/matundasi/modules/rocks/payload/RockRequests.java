package bytech.co.tz.matundasi.modules.rocks.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RockRequests(
        @NotBlank(message = "rock_code is required")
        @Size(max = 255)
        String rockCode,

        String description,

        @NotBlank(message = "regime is required")
        @Size(max = 255)
        String regime

)
{}
