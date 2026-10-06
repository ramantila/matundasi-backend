package bytech.co.tz.matundasi.modules.core_size.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.core_size.payload.CoreSizeRequests;
import bytech.co.tz.matundasi.modules.core_size.response.CoreSizeResponse;
import bytech.co.tz.matundasi.modules.core_size.service.CoreSizeService;
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
@RequestMapping("/api/v1/core-sizes")
@RequiredArgsConstructor
@Tag(name = "Core Sizes", description = "Core size management API")
public class CoreSizeController {
    private final CoreSizeService coreSizeService;

    @PostMapping
    @Operation(summary = "Create a new core size interval",
            description = "Registers a new core size interval for a hole. Fails with 400 if core_to is not " +
                    "greater than core_from, and 409 if the hole already has an active interval at the same core_from.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Core size created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Interval already exists for this hole at this core_from"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CoreSizeResponse>> create(@Valid @RequestBody CoreSizeRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Core size created successfully.", HttpStatus.CREATED, coreSizeService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List core sizes",
            description = "Returns all active (non-deleted) core size intervals sorted by hole, then core_from.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Core sizes returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<CoreSizeResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Core sizes retrieved successfully.", HttpStatus.OK, coreSizeService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a core size by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Core size returned"),
            @ApiResponse(responseCode = "404", description = "Core size not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CoreSizeResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Core size retrieved successfully.", HttpStatus.OK, coreSizeService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a core size interval",
            description = "Replaces all fields of an existing core size interval. Fails with 409 if the hole " +
                    "already has another active interval at the new core_from.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Core size updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Core size not found"),
            @ApiResponse(responseCode = "409", description = "Interval already exists for this hole at this core_from"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CoreSizeResponse>> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody CoreSizeRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Core size updated successfully.", HttpStatus.OK, coreSizeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a core size", description = "Soft-deletes the core size interval (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Core size deleted"),
            @ApiResponse(responseCode = "404", description = "Core size not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        coreSizeService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Core size deleted successfully.", HttpStatus.OK));
    }
}
