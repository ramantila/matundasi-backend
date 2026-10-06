package bytech.co.tz.matundasi.modules.per_area_hole.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.per_area_hole.payload.PerAreaHoleRequests;
import bytech.co.tz.matundasi.modules.per_area_hole.response.PerAreaHoleResponse;
import bytech.co.tz.matundasi.modules.per_area_hole.service.PerAreaHoleService;
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
@RequestMapping("/api/v1/per-area-holes")
@RequiredArgsConstructor
@Tag(name = "Per Area Holes", description = "Hole names per area and drill type management API")
public class PerAreaHoleController {
    private final PerAreaHoleService perAreaHoleService;

    @PostMapping
    @Operation(summary = "Create a new per area hole record",
            description = "Registers hole names per area (Buh, Kilamo, Nyase) and drill type. Fails with 400 if " +
                    "no hole is provided.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Per area hole record created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PerAreaHoleResponse>> create(@Valid @RequestBody PerAreaHoleRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Per area hole record created successfully.", HttpStatus.CREATED, perAreaHoleService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List per area hole records",
            description = "Returns all active (non-deleted) per area hole records in the order they were created.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Per area hole records returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<PerAreaHoleResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Per area hole records retrieved successfully.", HttpStatus.OK, perAreaHoleService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a per area hole record by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Per area hole record returned"),
            @ApiResponse(responseCode = "404", description = "Per area hole record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PerAreaHoleResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Per area hole record retrieved successfully.", HttpStatus.OK, perAreaHoleService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a per area hole record",
            description = "Replaces all hole names of an existing record. Fails with 400 if no hole is provided.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Per area hole record updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Per area hole record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PerAreaHoleResponse>> update(@PathVariable UUID id,
                                                                    @Valid @RequestBody PerAreaHoleRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Per area hole record updated successfully.", HttpStatus.OK, perAreaHoleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a per area hole record",
            description = "Soft-deletes the per area hole record (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Per area hole record deleted"),
            @ApiResponse(responseCode = "404", description = "Per area hole record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        perAreaHoleService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Per area hole record deleted successfully.", HttpStatus.OK));
    }
}
