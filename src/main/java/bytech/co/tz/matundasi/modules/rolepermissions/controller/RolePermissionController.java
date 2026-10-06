package bytech.co.tz.matundasi.modules.rolepermissions.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.rolepermissions.payload.RolePermissionRequest;
import bytech.co.tz.matundasi.modules.rolepermissions.response.RolePermissionResponse;
import bytech.co.tz.matundasi.modules.rolepermissions.service.RolePermissionService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role-permissions")
@RequiredArgsConstructor
@Tag(name = "Role Permissions", description = "Assigns permissions to roles")
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;

    @PostMapping
    @Operation(summary = "Assign a permission to a role",
            description = "Fails with 404 if the role or permission does not exist, and 409 if the role already " +
                    "has this permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Role permission created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Role or permission not found"),
            @ApiResponse(responseCode = "409", description = "Permission already assigned to this role"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RolePermissionResponse>> create(@Valid @RequestBody RolePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Role permission created successfully.", HttpStatus.CREATED,
                        rolePermissionService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List role permissions",
            description = "Returns all active role-permission assignments, sorted by role then permission name. " +
                    "Pass roleId to list only one role's permissions.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role permissions returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<RolePermissionResponse>>> findAll(
            @RequestParam(required = false) UUID roleId) {
        return ResponseEntity.ok(new HttpResponse<>(true, "Role permissions retrieved successfully.", HttpStatus.OK,
                rolePermissionService.findAll(roleId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role permission by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role permission returned"),
            @ApiResponse(responseCode = "404", description = "Role permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RolePermissionResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(new HttpResponse<>(true, "Role permission retrieved successfully.", HttpStatus.OK,
                rolePermissionService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a permission from a role", description = "Soft-deletes the assignment (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role permission deleted"),
            @ApiResponse(responseCode = "404", description = "Role permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        rolePermissionService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Role permission deleted successfully.", HttpStatus.OK));
    }
}
