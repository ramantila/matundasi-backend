package bytech.co.tz.matundasi.modules.users.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.users.payload.ChangePasswordRequest;
import bytech.co.tz.matundasi.modules.users.payload.UserRequest;
import bytech.co.tz.matundasi.modules.users.response.UserResponse;
import bytech.co.tz.matundasi.modules.users.service.UserService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user",
            description = "Registers a new user in a branch (branch_id is required), optionally assigned roles via role_ids. " +
                    "Fails with 400 if password is missing, 404 if a branch or role does not exist, and 409 if " +
                    "the username or phone is already used by another active user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Branch or role not found"),
            @ApiResponse(responseCode = "409", description = "Username or phone already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "User created successfully.", HttpStatus.CREATED, userService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns all active (non-deleted) users sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Users retrieved successfully.", HttpStatus.OK, userService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User returned"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<UserResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "User retrieved successfully.", HttpStatus.OK, userService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user",
            description = "Replaces the details, branch and roles of an existing user. Password is ignored — use " +
                    "PATCH /users/{id}/change-password. Fails with 409 if the new username or phone is already used " +
                    "by another active user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "User, branch or role not found"),
            @ApiResponse(responseCode = "409", description = "Username or phone already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<UserResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "User updated successfully.", HttpStatus.OK, userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Soft-deletes the user (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "User deleted successfully.", HttpStatus.OK));
    }

    @PatchMapping("/{id}/change-password")
    @Operation(summary = "Reset a user's password",
            description = "Admin-driven reset — sets the new password directly, no current-password check.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> changePassword(@PathVariable UUID id,
                                                             @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(new HttpResponse<>(true, "Password changed successfully.", HttpStatus.OK));
    }
}
