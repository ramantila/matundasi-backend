package bytech.co.tz.matundasi.modules.colours.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ColourRequests(
        @NotBlank(message = "colour_code is required")
        @Size(max = 255)
        String colourCode,

        @NotBlank(message = "colour_name is required")
        @Size(max = 255)
        String colourName

)
{}
