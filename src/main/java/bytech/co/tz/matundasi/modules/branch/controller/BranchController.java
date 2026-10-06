package bytech.co.tz.matundasi.modules.branch.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.branch.payload.BranchRequests;
import bytech.co.tz.matundasi.modules.branch.response.BranchResponse;
import bytech.co.tz.matundasi.modules.branch.service.BranchService;
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
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Branch management API")
public class BranchController {
    private final BranchService branchService;

    @PostMapping
    @Operation(summary = "Create a new branch", description = "Registers a new branch.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Branch created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<BranchResponse>> create(@Valid @RequestBody BranchRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Branch created successfully.", HttpStatus.CREATED, branchService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List branches", description = "Returns all active (non-deleted) branches sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branches returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<BranchResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Branches retrieved successfully.", HttpStatus.OK, branchService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a branch by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch returned"),
            @ApiResponse(responseCode = "404", description = "Branch not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<BranchResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Branch retrieved successfully.", HttpStatus.OK, branchService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a branch", description = "Replaces the name of an existing branch.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Branch not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<BranchResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody BranchRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Branch updated successfully.", HttpStatus.OK, branchService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a branch",
            description = "Soft-deletes the branch (sets deleted_at). Fails with 409 if it still has users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch deleted"),
            @ApiResponse(responseCode = "404", description = "Branch not found"),
            @ApiResponse(responseCode = "409", description = "Branch still has users"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        branchService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Branch deleted successfully.", HttpStatus.OK));
    }
}
