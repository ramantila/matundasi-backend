package bytech.co.tz.matundasi.modules.qualifiers.repository;

import bytech.co.tz.matundasi.modules.qualifiers.entity.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QualifierRepository extends JpaRepository<Qualifier, UUID> {
    List<Qualifier> findAllByDeletedAtIsNullOrderByQualifierCodeAsc();

    Optional<Qualifier> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByQualifierCodeIgnoreCaseAndDeletedAtIsNull(String qualifierCode);

    boolean existsByQualifierCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String qualifierCode, UUID id);
}
