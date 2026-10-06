package bytech.co.tz.matundasi.modules.drill.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.drill.payload.DrillRequests;
import bytech.co.tz.matundasi.modules.drill.response.DrillResponse;
import bytech.co.tz.matundasi.modules.drill.service.DrillService;
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
@RequestMapping("/api/v1/drills")
@RequiredArgsConstructor
@Tag(name = "Drills", description = "Drill sample management API")
public class DrillController {
    private final DrillService drillService;

    @PostMapping
    @Operation(summary = "Create a new drill record",
            description = "Registers a new drill sample interval linked to an element. Fails with 400 if " +
                    "to_depth is not greater than from_depth, and 404 if the element does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Drill record created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillResponse>> create(@Valid @RequestBody DrillRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Drill record created successfully.", HttpStatus.CREATED, drillService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List drill records",
            description = "Returns all active (non-deleted) drill records sorted by hole, then order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill records returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<DrillResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill records retrieved successfully.", HttpStatus.OK, drillService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a drill record by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill record returned"),
            @ApiResponse(responseCode = "404", description = "Drill record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill record retrieved successfully.", HttpStatus.OK, drillService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a drill record",
            description = "Replaces all fields of an existing drill record, including its element.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill record updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Drill record or element not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DrillResponse>> update(@PathVariable UUID id,
                                                              @Valid @RequestBody DrillRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Drill record updated successfully.", HttpStatus.OK, drillService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a drill record", description = "Soft-deletes the drill record (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Drill record deleted"),
            @ApiResponse(responseCode = "404", description = "Drill record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        drillService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Drill record deleted successfully.", HttpStatus.OK));
    }
}
