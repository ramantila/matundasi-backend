package bytech.co.tz.matundasi.modules.dispatch.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.dispatch.payload.DispatchRequests;
import bytech.co.tz.matundasi.modules.dispatch.response.DispatchResponse;
import bytech.co.tz.matundasi.modules.dispatch.service.DispatchService;
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
@RequestMapping("/api/v1/dispatches")
@RequiredArgsConstructor
@Tag(name = "Dispatches", description = "Sample dispatch management API")
public class DispatchController {
    private final DispatchService dispatchService;

    @PostMapping
    @Operation(summary = "Create a new dispatch",
            description = "Registers a new sample dispatch, optionally linked to a project and an invoice. " +
                    "Fails with 400 if number_assayed exceeds number_of_samples or date_received is before " +
                    "dispatch_date, and 404 if the project or invoice does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dispatch created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Project or invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DispatchResponse>> create(@Valid @RequestBody DispatchRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Dispatch created successfully.", HttpStatus.CREATED, dispatchService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List dispatches",
            description = "Returns all active (non-deleted) dispatches sorted by order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispatches returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<DispatchResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Dispatches retrieved successfully.", HttpStatus.OK, dispatchService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a dispatch by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispatch returned"),
            @ApiResponse(responseCode = "404", description = "Dispatch not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DispatchResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Dispatch retrieved successfully.", HttpStatus.OK, dispatchService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dispatch", description = "Replaces all fields of an existing dispatch.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispatch updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Dispatch, project or invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<DispatchResponse>> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody DispatchRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Dispatch updated successfully.", HttpStatus.OK, dispatchService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dispatch", description = "Soft-deletes the dispatch (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispatch deleted"),
            @ApiResponse(responseCode = "404", description = "Dispatch not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        dispatchService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Dispatch deleted successfully.", HttpStatus.OK));
    }
}
