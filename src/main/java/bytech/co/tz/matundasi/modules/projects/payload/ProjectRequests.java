package bytech.co.tz.matundasi.modules.projects.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequests(
        @NotBlank(message = "name is required")
        @Size(max = 255)
        String name,

        @NotBlank(message = "tenement is required")
        @Size(max = 255)
        String tenement,

        @NotBlank(message = "cost_code is required")
        @Size(max = 255)
        String costCode

)
{}
