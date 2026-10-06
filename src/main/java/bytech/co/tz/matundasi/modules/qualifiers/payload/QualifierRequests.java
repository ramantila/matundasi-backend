package bytech.co.tz.matundasi.modules.qualifiers.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QualifierRequests(
        @NotBlank(message = "qualifier_code is required")
        @Size(max = 255)
        String qualifierCode,

        String description

)
{}
