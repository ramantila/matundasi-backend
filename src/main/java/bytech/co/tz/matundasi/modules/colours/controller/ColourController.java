package bytech.co.tz.matundasi.modules.colours.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.colours.payload.ColourRequests;
import bytech.co.tz.matundasi.modules.colours.response.ColourResponse;
import bytech.co.tz.matundasi.modules.colours.service.ColourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/colours")
@RequiredArgsConstructor
@Tag(name = "Colours", description = "Colour management API")
public class ColourController {
    private final ColourService colourService;

    @PostMapping
    @Operation(summary = "Create a new colour",
            description = "Registers a new colour. Fails with 409 if the code or name is already used by " +
                    "another active colour.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colour created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Colour code or name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ColourResponse>> create(@Valid @RequestBody ColourRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Colour created successfully.", HttpStatus.CREATED, colourService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List colours", description = "Returns all active (non-deleted) colours sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colours returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<ColourResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Colours retrieved successfully.", HttpStatus.OK, colourService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a colour by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colour returned"),
            @ApiResponse(responseCode = "404", description = "Colour not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ColourResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Colour retrieved successfully.", HttpStatus.OK, colourService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a colour",
            description = "Replaces the code and name of an existing colour. Fails with 409 if the new code " +
                    "or name is already used by another active colour.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colour updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Colour not found"),
            @ApiResponse(responseCode = "409", description = "Colour code or name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ColourResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody ColourRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Colour updated successfully.", HttpStatus.OK, colourService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a colour", description = "Soft-deletes the colour (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colour deleted"),
            @ApiResponse(responseCode = "404", description = "Colour not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        colourService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Colour deleted successfully.", HttpStatus.OK));
    }
}
