package bytech.co.tz.matundasi.modules.collar.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.collar.payload.CollarRequests;
import bytech.co.tz.matundasi.modules.collar.response.CollarResponse;
import bytech.co.tz.matundasi.modules.collar.service.CollarService;
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
@RequestMapping("/api/v1/collars")
@RequiredArgsConstructor
@Tag(name = "Collars", description = "Drill hole collar management API")
public class CollarController {
    private final CollarService collarService;

    @PostMapping
    @Operation(summary = "Create a new collar",
            description = "Registers a new drill hole collar. Fails with 409 if a collar already exists for the hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Collar created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Collar for this hole already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CollarResponse>> create(@Valid @RequestBody CollarRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Collar created successfully.", HttpStatus.CREATED, collarService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List collars", description = "Returns all active (non-deleted) collars sorted by hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Collars returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<CollarResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Collars retrieved successfully.", HttpStatus.OK, collarService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a collar by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Collar returned"),
            @ApiResponse(responseCode = "404", description = "Collar not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CollarResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Collar retrieved successfully.", HttpStatus.OK, collarService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a collar",
            description = "Replaces all fields of an existing collar. Fails with 409 if another active collar " +
                    "already uses the new hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Collar updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Collar not found"),
            @ApiResponse(responseCode = "409", description = "Collar for this hole already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<CollarResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody CollarRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Collar updated successfully.", HttpStatus.OK, collarService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a collar", description = "Soft-deletes the collar (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Collar deleted"),
            @ApiResponse(responseCode = "404", description = "Collar not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        collarService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Collar deleted successfully.", HttpStatus.OK));
    }
}
