package bytech.co.tz.matundasi.modules.grids.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.grids.payload.GridRequests;
import bytech.co.tz.matundasi.modules.grids.response.GridResponse;
import bytech.co.tz.matundasi.modules.grids.service.GridService;
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
@RequestMapping("/api/v1/grids")
@RequiredArgsConstructor
@Tag(name = "Grids", description = "Grid transformation management API")
public class GridController {
    private final GridService gridService;

    @PostMapping
    @Operation(summary = "Create a new grid",
            description = "Registers a new grid with its control points in the original and derived grids. " +
                    "Fails with 409 if the local name is already used by another active grid.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grid created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Grid local name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<GridResponse>> create(@Valid @RequestBody GridRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Grid created successfully.", HttpStatus.CREATED, gridService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List grids", description = "Returns all active (non-deleted) grids sorted by local name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grids returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<GridResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Grids retrieved successfully.", HttpStatus.OK, gridService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a grid by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grid returned"),
            @ApiResponse(responseCode = "404", description = "Grid not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<GridResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Grid retrieved successfully.", HttpStatus.OK, gridService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a grid",
            description = "Replaces all fields of an existing grid. Fails with 409 if the new local name is " +
                    "already used by another active grid.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grid updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Grid not found"),
            @ApiResponse(responseCode = "409", description = "Grid local name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<GridResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody GridRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Grid updated successfully.", HttpStatus.OK, gridService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grid", description = "Soft-deletes the grid (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grid deleted"),
            @ApiResponse(responseCode = "404", description = "Grid not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        gridService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Grid deleted successfully.", HttpStatus.OK));
    }
}
