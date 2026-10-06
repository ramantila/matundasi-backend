package bytech.co.tz.matundasi.modules.compos_au.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.compos_au.payload.ComposAuRequests;
import bytech.co.tz.matundasi.modules.compos_au.response.ComposAuResponse;
import bytech.co.tz.matundasi.modules.compos_au.service.ComposAuService;
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
@RequestMapping("/api/v1/compos-au")
@RequiredArgsConstructor
@Tag(name = "Au Composites", description = "Gold composite management API")
public class ComposAuController {
    private final ComposAuService composAuService;

    @PostMapping
    @Operation(summary = "Create a new Au composite",
            description = "Registers a new gold composite interval. length is calculated as to_depth - from_depth. " +
                    "Fails with 400 if to_depth is not greater than from_depth, and 409 if the hole already has an " +
                    "active Au composite at the same from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Au composite created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Au composite already exists for this hole at this from_depth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ComposAuResponse>> create(@Valid @RequestBody ComposAuRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Au composite created successfully.", HttpStatus.CREATED, composAuService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List Au composites",
            description = "Returns all active (non-deleted) Au composites sorted by hole, then from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Au composites returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<ComposAuResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Au composites retrieved successfully.", HttpStatus.OK, composAuService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an Au composite by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Au composite returned"),
            @ApiResponse(responseCode = "404", description = "Au composite not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ComposAuResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Au composite retrieved successfully.", HttpStatus.OK, composAuService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an Au composite",
            description = "Replaces all fields of an existing Au composite and recalculates length. Fails with 409 " +
                    "if the hole already has another active Au composite at the new from_depth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Au composite updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Au composite not found"),
            @ApiResponse(responseCode = "409", description = "Au composite already exists for this hole at this from_depth"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ComposAuResponse>> update(@PathVariable UUID id,
                                                                 @Valid @RequestBody ComposAuRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Au composite updated successfully.", HttpStatus.OK, composAuService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Au composite", description = "Soft-deletes the Au composite (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Au composite deleted"),
            @ApiResponse(responseCode = "404", description = "Au composite not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        composAuService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Au composite deleted successfully.", HttpStatus.OK));
    }
}
