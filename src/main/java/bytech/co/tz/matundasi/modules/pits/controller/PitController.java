package bytech.co.tz.matundasi.modules.pits.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.pits.payload.PitRequests;
import bytech.co.tz.matundasi.modules.pits.response.PitResponse;
import bytech.co.tz.matundasi.modules.pits.service.PitService;
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
@RequestMapping("/api/v1/pits")
@RequiredArgsConstructor
@Tag(name = "Pits", description = "Pit sample management API")
public class PitController {
    private final PitService pitService;

    @PostMapping
    @Operation(summary = "Create a new pit record",
            description = "Registers a new pit sample interval, optionally linked to a lithology and a sample. " +
                    "Fails with 400 if to_depth is not greater than from_depth, and 404 if a linked lithology or " +
                    "sample does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pit record created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Lithology or sample not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PitResponse>> create(@Valid @RequestBody PitRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Pit record created successfully.", HttpStatus.CREATED, pitService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List pit records",
            description = "Returns all active (non-deleted) pit records sorted by pit number, then order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pit records returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<PitResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Pit records retrieved successfully.", HttpStatus.OK, pitService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a pit record by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pit record returned"),
            @ApiResponse(responseCode = "404", description = "Pit record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PitResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Pit record retrieved successfully.", HttpStatus.OK, pitService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a pit record", description = "Replaces all fields of an existing pit record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pit record updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Pit record, lithology or sample not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PitResponse>> update(@PathVariable UUID id,
                                                            @Valid @RequestBody PitRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Pit record updated successfully.", HttpStatus.OK, pitService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a pit record", description = "Soft-deletes the pit record (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pit record deleted"),
            @ApiResponse(responseCode = "404", description = "Pit record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        pitService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Pit record deleted successfully.", HttpStatus.OK));
    }
}
