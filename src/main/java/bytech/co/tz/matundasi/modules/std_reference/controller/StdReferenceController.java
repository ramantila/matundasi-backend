package bytech.co.tz.matundasi.modules.std_reference.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.std_reference.payload.StdReferenceRequests;
import bytech.co.tz.matundasi.modules.std_reference.response.StdReferenceResponse;
import bytech.co.tz.matundasi.modules.std_reference.service.StdReferenceService;
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
@RequestMapping("/api/v1/std-references")
@RequiredArgsConstructor
@Tag(name = "Standard References", description = "Assay standard reference value management API")
public class StdReferenceController {
    private final StdReferenceService stdReferenceService;

    @PostMapping
    @Operation(summary = "Create a new standard reference",
            description = "Registers a new assay standard with its certified reference values. Fails with 409 if " +
                    "the standard is already registered.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Standard reference created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Standard already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<StdReferenceResponse>> create(@Valid @RequestBody StdReferenceRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Standard reference created successfully.", HttpStatus.CREATED, stdReferenceService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List standard references",
            description = "Returns all active (non-deleted) standard references sorted by standard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Standard references returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<StdReferenceResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Standard references retrieved successfully.", HttpStatus.OK, stdReferenceService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a standard reference by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Standard reference returned"),
            @ApiResponse(responseCode = "404", description = "Standard reference not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<StdReferenceResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Standard reference retrieved successfully.", HttpStatus.OK, stdReferenceService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a standard reference",
            description = "Replaces all fields of an existing standard reference. Fails with 409 if the new " +
                    "standard is already registered.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Standard reference updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Standard reference not found"),
            @ApiResponse(responseCode = "409", description = "Standard already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<StdReferenceResponse>> update(@PathVariable UUID id,
                                                                     @Valid @RequestBody StdReferenceRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Standard reference updated successfully.", HttpStatus.OK, stdReferenceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a standard reference",
            description = "Soft-deletes the standard reference (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Standard reference deleted"),
            @ApiResponse(responseCode = "404", description = "Standard reference not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        stdReferenceService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Standard reference deleted successfully.", HttpStatus.OK));
    }
}
