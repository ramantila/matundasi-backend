package bytech.co.tz.matundasi.modules.schs.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.schs.payload.SchistosityRequests;
import bytech.co.tz.matundasi.modules.schs.response.SchistosityResponse;
import bytech.co.tz.matundasi.modules.schs.service.SchistosityService;
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
@RequestMapping("/api/v1/schistosity")
@RequiredArgsConstructor
@Tag(name = "Schistosity", description = "Schistosity management API")
public class SchistosityController {
    private final SchistosityService schistosityService;

    @PostMapping
    @Operation(summary = "Create a new schistosity",
            description = "Registers a new schistosity. Fails with 409 if the code is already used by another " +
                    "active schistosity.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Schistosity created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Schistosity code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SchistosityResponse>> create(@Valid @RequestBody SchistosityRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Schistosity created successfully.", HttpStatus.CREATED, schistosityService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List schistosity",
            description = "Returns all active (non-deleted) schistosity sorted by code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schistosity returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<SchistosityResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Schistosity records retrieved successfully.", HttpStatus.OK, schistosityService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a schistosity by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schistosity returned"),
            @ApiResponse(responseCode = "404", description = "Schistosity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SchistosityResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Schistosity retrieved successfully.", HttpStatus.OK, schistosityService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a schistosity",
            description = "Replaces the code and schistosity of an existing record. Fails with 409 if the new " +
                    "code is already used by another active schistosity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schistosity updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Schistosity not found"),
            @ApiResponse(responseCode = "409", description = "Schistosity code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<SchistosityResponse>> update(@PathVariable UUID id,
                                                                    @Valid @RequestBody SchistosityRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Schistosity updated successfully.", HttpStatus.OK, schistosityService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a schistosity", description = "Soft-deletes the schistosity (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schistosity deleted"),
            @ApiResponse(responseCode = "404", description = "Schistosity not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        schistosityService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Schistosity deleted successfully.", HttpStatus.OK));
    }
}
