package bytech.co.tz.matundasi.modules.rab_collar.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.rab_collar.payload.RabCollarRequests;
import bytech.co.tz.matundasi.modules.rab_collar.response.RabCollarResponse;
import bytech.co.tz.matundasi.modules.rab_collar.service.RabCollarService;
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
@RequestMapping("/api/v1/rab-collars")
@RequiredArgsConstructor
@Tag(name = "RAB Collars", description = "RAB drill hole collar management API")
public class RabCollarController {
    private final RabCollarService rabCollarService;

    @PostMapping
    @Operation(summary = "Create a new RAB collar",
            description = "Registers a new RAB hole collar. Fails with 409 if a RAB collar already exists for the hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "RAB collar created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "RAB collar for this hole already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RabCollarResponse>> create(@Valid @RequestBody RabCollarRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "RAB collar created successfully.", HttpStatus.CREATED, rabCollarService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List RAB collars", description = "Returns all active (non-deleted) RAB collars sorted by hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "RAB collars returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<RabCollarResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "RAB collars retrieved successfully.", HttpStatus.OK, rabCollarService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a RAB collar by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "RAB collar returned"),
            @ApiResponse(responseCode = "404", description = "RAB collar not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RabCollarResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "RAB collar retrieved successfully.", HttpStatus.OK, rabCollarService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a RAB collar",
            description = "Replaces all fields of an existing RAB collar. Fails with 409 if another active RAB " +
                    "collar already uses the new hole.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "RAB collar updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "RAB collar not found"),
            @ApiResponse(responseCode = "409", description = "RAB collar for this hole already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RabCollarResponse>> update(@PathVariable UUID id,
                                                                  @Valid @RequestBody RabCollarRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "RAB collar updated successfully.", HttpStatus.OK, rabCollarService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a RAB collar", description = "Soft-deletes the RAB collar (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "RAB collar deleted"),
            @ApiResponse(responseCode = "404", description = "RAB collar not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        rabCollarService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "RAB collar deleted successfully.", HttpStatus.OK));
    }
}
