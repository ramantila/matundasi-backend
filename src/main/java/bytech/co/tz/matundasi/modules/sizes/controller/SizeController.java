package bytech.co.tz.matundasi.modules.sizes.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.sizes.payload.SizeRequests;
import bytech.co.tz.matundasi.modules.sizes.response.SizeResponse;
import bytech.co.tz.matundasi.modules.sizes.service.SizeService;
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
@RequestMapping("/api/v1/sizes")
@RequiredArgsConstructor
@Tag(name = "Sizes", description = "Size management API")
public class SizeController {
    private final SizeService sizeService;

    @PostMapping
    @Operation(summary = "Create a new size",
            description = "Registers a new size. Fails with 409 if the code is already used by another " +
                    "active size.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Size created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Size code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SizeResponse>> create(@Valid @RequestBody SizeRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Size created successfully.", HttpStatus.CREATED, sizeService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List sizes", description = "Returns all active (non-deleted) sizes sorted by creation date (oldest first).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sizes returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<SizeResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Sizes retrieved successfully.", HttpStatus.OK, sizeService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a size by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Size returned"),
            @ApiResponse(responseCode = "404", description = "Size not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SizeResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Size retrieved successfully.", HttpStatus.OK, sizeService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a size",
            description = "Replaces the code and grain of an existing size. Fails with 409 if the new " +
                    "code is already used by another active size.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Size updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Size not found"),
            @ApiResponse(responseCode = "409", description = "Size code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SizeResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody SizeRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Size updated successfully.", HttpStatus.OK, sizeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a size", description = "Soft-deletes the size (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Size deleted"),
            @ApiResponse(responseCode = "404", description = "Size not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        sizeService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Size deleted successfully.", HttpStatus.OK));
    }
}
