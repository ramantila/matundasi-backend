package bytech.co.tz.matundasi.modules.composite.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.composite.payload.CompositeRequests;
import bytech.co.tz.matundasi.modules.composite.response.CompositeResponse;
import bytech.co.tz.matundasi.modules.composite.service.CompositeService;
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
@RequestMapping("/api/v1/composites")
@RequiredArgsConstructor
@Tag(name = "Composites", description = "Composite management API")
public class CompositeController {
    private final CompositeService compositeService;

    @PostMapping
    @Operation(summary = "Create a new composite",
            description = "Registers a new composite interval. length is calculated as to_depth - from_depth. " +
                    "Fails with 400 if to_depth is not greater than from_depth, and 409 if the hole already has an " +
                    "active composite at the same from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Composite created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Composite already exists for this hole at this from_depth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CompositeResponse>> create(@Valid @RequestBody CompositeRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Composite created successfully.", HttpStatus.CREATED, compositeService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List composites",
            description = "Returns all active (non-deleted) composites sorted by hole, then from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Composites returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<CompositeResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Composites retrieved successfully.", HttpStatus.OK, compositeService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a composite by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Composite returned"),
            @ApiResponse(responseCode = "404", description = "Composite not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CompositeResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Composite retrieved successfully.", HttpStatus.OK, compositeService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a composite",
            description = "Replaces all fields of an existing composite and recalculates length. Fails with 409 " +
                    "if the hole already has another active composite at the new from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Composite updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Composite not found"),
            @ApiResponse(responseCode = "409", description = "Composite already exists for this hole at this from_depth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CompositeResponse>> update(@PathVariable UUID id,
                                                                  @Valid @RequestBody CompositeRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Composite updated successfully.", HttpStatus.OK, compositeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a composite", description = "Soft-deletes the composite (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Composite deleted"),
            @ApiResponse(responseCode = "404", description = "Composite not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        compositeService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Composite deleted successfully.", HttpStatus.OK));
    }
}
