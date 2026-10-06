package bytech.co.tz.matundasi.modules.difference.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.difference.payload.DifferenceRequests;
import bytech.co.tz.matundasi.modules.difference.response.DifferenceResponse;
import bytech.co.tz.matundasi.modules.difference.service.DifferenceService;
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
@RequestMapping("/api/v1/differences")
@RequiredArgsConstructor
@Tag(name = "Differences", description = "Sample difference management API")
public class DifferenceController {
    private final DifferenceService differenceService;

    @PostMapping
    @Operation(summary = "Create a new difference",
            description = "Registers a new sample difference, optionally linked to a project and an invoice. " +
                    "Fails with 400 if number_assayed exceeds number_of_samples or date_received is before " +
                    "dispatch_date, and 404 if the project or invoice does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Difference created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Project or invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DifferenceResponse>> create(@Valid @RequestBody DifferenceRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Difference created successfully.", HttpStatus.CREATED, differenceService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List differences",
            description = "Returns all active (non-deleted) differences sorted by order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Differences returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<DifferenceResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Differences retrieved successfully.", HttpStatus.OK, differenceService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a difference by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Difference returned"),
            @ApiResponse(responseCode = "404", description = "Difference not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DifferenceResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Difference retrieved successfully.", HttpStatus.OK, differenceService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a difference", description = "Replaces all fields of an existing difference.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Difference updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Difference, project or invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DifferenceResponse>> update(@PathVariable UUID id,
                                                                   @Valid @RequestBody DifferenceRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Difference updated successfully.", HttpStatus.OK, differenceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a difference", description = "Soft-deletes the difference (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Difference deleted"),
            @ApiResponse(responseCode = "404", description = "Difference not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        differenceService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Difference deleted successfully.", HttpStatus.OK));
    }
}
