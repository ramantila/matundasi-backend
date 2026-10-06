package bytech.co.tz.matundasi.modules.lith.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.lith.payload.LithologyRequests;
import bytech.co.tz.matundasi.modules.lith.response.LithologyResponse;
import bytech.co.tz.matundasi.modules.lith.service.LithologyService;
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
@RequestMapping("/api/v1/lithology")
@RequiredArgsConstructor
@Tag(name = "Lithology", description = "Lithology log management API")
public class LithologyController {
    private final LithologyService lithologyService;

    @PostMapping
    @Operation(summary = "Create a new lithology interval",
            description = "Registers a new lithology interval, optionally linked to a weathering code. Fails with 400 if " +
                    "lithto is not greater than lithfrom, 404 if a given weathering code does not exist, and 409 if " +
                    "the hole already has an active interval at the same lithfrom.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lithology created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Weathering code not found"),
            @ApiResponse(responseCode = "409", description = "Interval already exists for this hole at this lithfrom"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<LithologyResponse>> create(@Valid @RequestBody LithologyRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Lithology created successfully.", HttpStatus.CREATED, lithologyService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List lithology intervals",
            description = "Returns all active (non-deleted) lithology intervals sorted by hole, then lithfrom.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lithology intervals returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<LithologyResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Lithology records retrieved successfully.", HttpStatus.OK, lithologyService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lithology interval by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lithology returned"),
            @ApiResponse(responseCode = "404", description = "Lithology not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<LithologyResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Lithology retrieved successfully.", HttpStatus.OK, lithologyService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a lithology interval",
            description = "Replaces all fields of an existing lithology interval, including its weathering code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lithology updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Lithology or weathering code not found"),
            @ApiResponse(responseCode = "409", description = "Interval already exists for this hole at this lithfrom"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<LithologyResponse>> update(@PathVariable UUID id,
                                                                  @Valid @RequestBody LithologyRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Lithology updated successfully.", HttpStatus.OK, lithologyService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a lithology interval",
            description = "Soft-deletes the lithology interval (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lithology deleted"),
            @ApiResponse(responseCode = "404", description = "Lithology not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        lithologyService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Lithology deleted successfully.", HttpStatus.OK));
    }
}
