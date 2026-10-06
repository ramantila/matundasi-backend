package bytech.co.tz.matundasi.modules.rocks.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.rocks.payload.RockRequests;
import bytech.co.tz.matundasi.modules.rocks.response.RockResponse;
import bytech.co.tz.matundasi.modules.rocks.service.RockService;
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
@RequestMapping("/api/v1/rocks")
@RequiredArgsConstructor
@Tag(name = "Rocks", description = "Rock management API")
public class RockController {
    private final RockService rockService;

    @PostMapping
    @Operation(summary = "Create a new rock",
            description = "Registers a new rock. Fails with 409 if the rock code is already used by another " +
                    "active rock.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rock created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Rock code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RockResponse>> create(@Valid @RequestBody RockRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Rock created successfully.", HttpStatus.CREATED, rockService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List rocks", description = "Returns all active (non-deleted) rocks sorted by rock code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rocks returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<RockResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Rocks retrieved successfully.", HttpStatus.OK, rockService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a rock by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rock returned"),
            @ApiResponse(responseCode = "404", description = "Rock not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RockResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Rock retrieved successfully.", HttpStatus.OK, rockService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a rock",
            description = "Replaces the rock code, description and regime of an existing rock. Fails with 409 " +
                    "if the new rock code is already used by another active rock.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rock updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Rock not found"),
            @ApiResponse(responseCode = "409", description = "Rock code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RockResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody RockRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Rock updated successfully.", HttpStatus.OK, rockService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a rock", description = "Soft-deletes the rock (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rock deleted"),
            @ApiResponse(responseCode = "404", description = "Rock not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        rockService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Rock deleted successfully.", HttpStatus.OK));
    }
}
