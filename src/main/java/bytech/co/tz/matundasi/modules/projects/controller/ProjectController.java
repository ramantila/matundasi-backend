package bytech.co.tz.matundasi.modules.projects.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.projects.payload.ProjectRequests;
import bytech.co.tz.matundasi.modules.projects.response.ProjectResponse;
import bytech.co.tz.matundasi.modules.projects.service.ProjectService;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management API")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project",
            description = "Registers a new project. Fails with 409 if the name is already used by another " +
                    "active project.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Project name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProjectResponse>> create(@Valid @RequestBody ProjectRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Project created successfully.", HttpStatus.CREATED, projectService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List projects", description = "Returns all active (non-deleted) projects sorted by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<ProjectResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Projects retrieved successfully.", HttpStatus.OK, projectService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project returned"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProjectResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Project retrieved successfully.", HttpStatus.OK, projectService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project",
            description = "Replaces the name, tenement and cost code of an existing project. Fails with 409 if " +
                    "the new name is already used by another active project.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "409", description = "Project name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<ProjectResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody ProjectRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Project updated successfully.", HttpStatus.OK, projectService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Soft-deletes the project (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        projectService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Project deleted successfully.", HttpStatus.OK));
    }
}
