package bytech.co.tz.matundasi.modules.grids.repository;

import bytech.co.tz.matundasi.modules.grids.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GridRepository extends JpaRepository<Grid, UUID> {
    List<Grid> findAllByDeletedAtIsNullOrderByLocalNameAsc();

    Optional<Grid> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByLocalNameIgnoreCaseAndDeletedAtIsNull(String localName);

    boolean existsByLocalNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String localName, UUID id);
}
