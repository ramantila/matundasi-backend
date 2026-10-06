package bytech.co.tz.matundasi.modules.weath.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.weath.payload.WeatheringRequests;
import bytech.co.tz.matundasi.modules.weath.response.WeatheringResponse;
import bytech.co.tz.matundasi.modules.weath.service.WeatheringService;
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
@RequestMapping("/api/v1/weathering")
@RequiredArgsConstructor
@Tag(name = "Weathering", description = "Weathering management API")
public class WeatheringController {
    private final WeatheringService weatheringService;

    @PostMapping
    @Operation(summary = "Create a new weathering",
            description = "Registers a new weathering. Fails with 409 if the code is already used by another " +
                    "active weathering.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Weathering created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Weathering code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<WeatheringResponse>> create(@Valid @RequestBody WeatheringRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Weathering created successfully.", HttpStatus.CREATED, weatheringService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List weathering",
            description = "Returns all active (non-deleted) weathering sorted by creation date.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weathering returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<WeatheringResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Weathering records retrieved successfully.", HttpStatus.OK, weatheringService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a weathering by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weathering returned"),
            @ApiResponse(responseCode = "404", description = "Weathering not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<WeatheringResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Weathering retrieved successfully.", HttpStatus.OK, weatheringService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a weathering",
            description = "Replaces the code and description of an existing weathering. Fails " +
                    "with 409 if the new code is already used by another active weathering.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weathering updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Weathering not found"),
            @ApiResponse(responseCode = "409", description = "Weathering code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<WeatheringResponse>> update(@PathVariable UUID id,
                                                                   @Valid @RequestBody WeatheringRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Weathering updated successfully.", HttpStatus.OK, weatheringService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a weathering", description = "Soft-deletes the weathering (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weathering deleted"),
            @ApiResponse(responseCode = "404", description = "Weathering not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        weatheringService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Weathering deleted successfully.", HttpStatus.OK));
    }
}
