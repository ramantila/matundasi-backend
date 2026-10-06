package bytech.co.tz.matundasi.modules.permissions.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.permissions.payload.PermissionRequest;
import bytech.co.tz.matundasi.modules.permissions.response.PermissionResponse;
import bytech.co.tz.matundasi.modules.permissions.service.PermissionService;
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
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "Permission catalog management API")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @Operation(summary = "Create a new permission",
            description = "Registers a new permission. Fails with 409 if the name is already used by another " +
                    "active permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Permission created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Permission name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PermissionResponse>> create(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Permission created successfully.", HttpStatus.CREATED, permissionService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List permissions", description = "Returns all active (non-deleted) permissions sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permissions returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<PermissionResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Permissions retrieved successfully.", HttpStatus.OK, permissionService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a permission by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission returned"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PermissionResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Permission retrieved successfully.", HttpStatus.OK, permissionService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a permission",
            description = "Renames an existing permission. Fails with 409 if the new name is already used by another " +
                    "active permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "409", description = "Permission name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<PermissionResponse>> update(@PathVariable UUID id,
                                                                   @Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Permission updated successfully.", HttpStatus.OK, permissionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a permission", description = "Soft-deletes the permission (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission deleted"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        permissionService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Permission deleted successfully.", HttpStatus.OK));
    }
}
