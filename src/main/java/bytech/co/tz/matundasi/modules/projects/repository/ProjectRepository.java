package bytech.co.tz.matundasi.modules.projects.repository;

import bytech.co.tz.matundasi.modules.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByDeletedAtIsNullOrderByNameAsc();

    Optional<Project> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    boolean existsByNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String name, UUID id);
}
