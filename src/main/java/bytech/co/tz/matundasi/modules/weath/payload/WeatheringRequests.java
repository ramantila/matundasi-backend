package bytech.co.tz.matundasi.modules.weath.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WeatheringRequests(
        @NotBlank(message = "code is required")
        @Size(max = 255)
        String code,

        String description

)
{}
