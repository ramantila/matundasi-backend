package bytech.co.tz.matundasi.modules.roles.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.roles.payload.RoleRequest;
import bytech.co.tz.matundasi.modules.roles.response.RoleResponse;
import bytech.co.tz.matundasi.modules.roles.service.RoleService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Role management API (RBAC)")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "Create a new role",
            description = "Registers a new role with a set of permissions. Fails with 409 if the name is already " +
                    "used by another active role, or 400 if any permission id is invalid.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Role created"),
            @ApiResponse(responseCode = "400", description = "Validation failed or invalid permission id"),
            @ApiResponse(responseCode = "409", description = "Role name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Role created successfully.", HttpStatus.CREATED, roleService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List roles",
            description = "Returns all active (non-deleted) roles sorted by name. Pass activeOnly=true to also " +
                    "exclude deactivated roles (used by the user form's role picker).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<RoleResponse>>> findAll(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Roles retrieved successfully.", HttpStatus.OK, roleService.findAll(activeOnly)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role returned"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RoleResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Role retrieved successfully.", HttpStatus.OK, roleService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a role",
            description = "Replaces the name, description and permissions of an existing role (active is only " +
                    "changed when sent). Fails with 409 if the new name is already used by another active role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed or invalid permission id"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "409", description = "Role name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<RoleResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Role updated successfully.", HttpStatus.OK, roleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role",
            description = "Soft-deletes the role (sets deleted_at). Fails with 409 if it is still assigned to a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role deleted"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "409", description = "Role is still assigned to a user"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        roleService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Role deleted successfully.", HttpStatus.OK));
    }
}
