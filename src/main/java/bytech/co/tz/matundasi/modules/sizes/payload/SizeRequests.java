package bytech.co.tz.matundasi.modules.sizes.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SizeRequests(
        @NotBlank(message = "code is required")
        @Size(max = 255)
        String code,

        @NotBlank(message = "grain is required")
        @Size(max = 255)
        String grain

)
{}
