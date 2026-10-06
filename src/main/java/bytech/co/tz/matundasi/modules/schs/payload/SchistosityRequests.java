package bytech.co.tz.matundasi.modules.schs.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SchistosityRequests(
        @NotBlank(message = "code is required")
        @Size(max = 255)
        String code,

        @NotBlank(message = "schistosity is required")
        @Size(max = 255)
        String schistosity

)
{}
