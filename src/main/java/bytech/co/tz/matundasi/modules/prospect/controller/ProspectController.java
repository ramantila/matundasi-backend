package bytech.co.tz.matundasi.modules.prospect.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.prospect.payload.ProspectRequests;
import bytech.co.tz.matundasi.modules.prospect.response.ProspectResponse;
import bytech.co.tz.matundasi.modules.prospect.service.ProspectService;
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
@RequestMapping("/api/v1/prospects")
@RequiredArgsConstructor
@Tag(name = "Prospects", description = "Prospect management API")
public class ProspectController {
    private final ProspectService prospectService;

    @PostMapping
    @Operation(summary = "Create a new prospect",
            description = "Registers a new prospect.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prospect created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProspectResponse>> create(@Valid @RequestBody ProspectRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Prospect created successfully.", HttpStatus.CREATED, prospectService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List prospects", description = "Returns all active (non-deleted) prospects sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prospects returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<ProspectResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Prospects retrieved successfully.", HttpStatus.OK, prospectService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a prospect by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prospect returned"),
            @ApiResponse(responseCode = "404", description = "Prospect not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProspectResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Prospect retrieved successfully.", HttpStatus.OK, prospectService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a prospect",
            description = "Replaces all fields of an existing prospect.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prospect updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Prospect not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProspectResponse>> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody ProspectRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Prospect updated successfully.", HttpStatus.OK, prospectService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prospect", description = "Soft-deletes the prospect (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prospect deleted"),
            @ApiResponse(responseCode = "404", description = "Prospect not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        prospectService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Prospect deleted successfully.", HttpStatus.OK));
    }
}
