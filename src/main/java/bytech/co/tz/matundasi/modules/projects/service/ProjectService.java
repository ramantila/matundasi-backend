package bytech.co.tz.matundasi.modules.projects.service;

import bytech.co.tz.matundasi.modules.projects.entity.Project;
import bytech.co.tz.matundasi.modules.projects.payload.ProjectRequests;
import bytech.co.tz.matundasi.modules.projects.repository.ProjectRepository;
import bytech.co.tz.matundasi.modules.projects.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(ProjectRequests request) {
        String name = request.name().trim();

        if (projectRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project name already exists");
        }

        Project project = Project.builder()
                .name(name)
                .tenement(request.tenement().trim())
                .costCode(request.costCode().trim())
                .build();

        return toResponse(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(UUID id) {
        return toResponse(getActiveProject(id));
    }

    @Transactional
    public ProjectResponse update(UUID id, ProjectRequests request) {
        Project project = getActiveProject(id);
        String name = request.name().trim();

        if (projectRepository.existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(name, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project name already exists");
        }

        project.setName(name);
        project.setTenement(request.tenement().trim());
        project.setCostCode(request.costCode().trim());

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(UUID id) {
        Project project = getActiveProject(id);
        project.softDelete();
        projectRepository.save(project);
    }

    private Project getActiveProject(UUID id) {
        return projectRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getTenement(),
                project.getCostCode()
        );
    }
}
