package bytech.co.tz.matundasi.modules.mw_s.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.mw_s.payload.MwsRequests;
import bytech.co.tz.matundasi.modules.mw_s.response.MwsResponse;
import bytech.co.tz.matundasi.modules.mw_s.service.MwsService;
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
@RequestMapping("/api/v1/mw-s")
@RequiredArgsConstructor
@Tag(name = "MW_S", description = "MW_S management API")
public class MwsController {
    private final MwsService mwsService;

    @PostMapping
    @Operation(summary = "Create a new MW_S record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "MW_S record created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<MwsResponse>> create(@Valid @RequestBody MwsRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "MWS created successfully.", HttpStatus.CREATED, mwsService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List MW_S records",
            description = "Returns all active (non-deleted) MW_S records sorted by hole, then hdepth.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MW_S records returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<MwsResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "MWS records retrieved successfully.", HttpStatus.OK, mwsService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an MW_S record by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MW_S record returned"),
            @ApiResponse(responseCode = "404", description = "MW_S record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<MwsResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "MWS retrieved successfully.", HttpStatus.OK, mwsService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an MW_S record",
            description = "Replaces the hole, type and hdepth of an existing MW_S record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MW_S record updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "MW_S record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<MwsResponse>> update(@PathVariable UUID id,
                                                            @Valid @RequestBody MwsRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "MWS updated successfully.", HttpStatus.OK, mwsService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an MW_S record", description = "Soft-deletes the MW_S record (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "MW_S record deleted"),
            @ApiResponse(responseCode = "404", description = "MW_S record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        mwsService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "MWS deleted successfully.", HttpStatus.OK));
    }
}
