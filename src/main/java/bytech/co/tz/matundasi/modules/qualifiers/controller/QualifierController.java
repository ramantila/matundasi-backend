package bytech.co.tz.matundasi.modules.qualifiers.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.qualifiers.payload.QualifierRequests;
import bytech.co.tz.matundasi.modules.qualifiers.response.QualifierResponse;
import bytech.co.tz.matundasi.modules.qualifiers.service.QualifierService;
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
@RequestMapping("/api/v1/qualifiers")
@RequiredArgsConstructor
@Tag(name = "Qualifiers", description = "Qualifier management API")
public class QualifierController {
    private final QualifierService qualifierService;

    @PostMapping
    @Operation(summary = "Create a new qualifier",
            description = "Registers a new qualifier. Fails with 409 if the qualifier code is already used by " +
                    "another active qualifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Qualifier created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Qualifier code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<QualifierResponse>> create(@Valid @RequestBody QualifierRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Qualifier created successfully.", HttpStatus.CREATED, qualifierService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List qualifiers",
            description = "Returns all active (non-deleted) qualifiers sorted by qualifier code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Qualifiers returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<QualifierResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Qualifiers retrieved successfully.", HttpStatus.OK, qualifierService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a qualifier by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Qualifier returned"),
            @ApiResponse(responseCode = "404", description = "Qualifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<QualifierResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Qualifier retrieved successfully.", HttpStatus.OK, qualifierService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a qualifier",
            description = "Replaces the qualifier code and description of an existing qualifier. Fails with 409 " +
                    "if the new qualifier code is already used by another active qualifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Qualifier updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Qualifier not found"),
            @ApiResponse(responseCode = "409", description = "Qualifier code already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<QualifierResponse>> update(@PathVariable UUID id,
                                                                  @Valid @RequestBody QualifierRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Qualifier updated successfully.", HttpStatus.OK, qualifierService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a qualifier", description = "Soft-deletes the qualifier (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Qualifier deleted"),
            @ApiResponse(responseCode = "404", description = "Qualifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        qualifierService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Qualifier deleted successfully.", HttpStatus.OK));
    }
}
