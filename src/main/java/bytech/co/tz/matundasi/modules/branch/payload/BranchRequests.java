package bytech.co.tz.matundasi.modules.branch.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequests(
        @NotBlank(message = "name is required")
        @Size(max = 255)
        String name

)
{}
