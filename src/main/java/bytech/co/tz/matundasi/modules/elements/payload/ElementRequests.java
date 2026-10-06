package bytech.co.tz.matundasi.modules.elements.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ElementRequests(
        @NotBlank(message = "element_name is required")
        @Size(max = 255)
        String elementName,

        @NotBlank(message = "symbol is required")
        @Size(max = 255)
        String symbol

)
{}
