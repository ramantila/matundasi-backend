package bytech.co.tz.matundasi.modules.sample.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.sample.payload.SampleRequests;
import bytech.co.tz.matundasi.modules.sample.response.SampleResponse;
import bytech.co.tz.matundasi.modules.sample.service.SampleService;
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
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
@Tag(name = "Samples", description = "Sample management API")
public class SampleController {
    private final SampleService sampleService;

    @PostMapping
    @Operation(summary = "Create a new sample",
            description = "Registers a new sample, optionally linked to an element. Fails with 404 if a given element does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sample created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SampleResponse>> create(@Valid @RequestBody SampleRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Sample created successfully.", HttpStatus.CREATED, sampleService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List samples",
            description = "Returns all active (non-deleted) samples sorted by order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Samples returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<SampleResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Samples retrieved successfully.", HttpStatus.OK, sampleService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sample by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sample returned"),
            @ApiResponse(responseCode = "404", description = "Sample not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SampleResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Sample retrieved successfully.", HttpStatus.OK, sampleService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sample",
            description = "Replaces all fields of an existing sample, including its element.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sample updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Sample or element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SampleResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody SampleRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Sample updated successfully.", HttpStatus.OK, sampleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sample", description = "Soft-deletes the sample (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sample deleted"),
            @ApiResponse(responseCode = "404", description = "Sample not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        sampleService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Sample deleted successfully.", HttpStatus.OK));
    }
}
