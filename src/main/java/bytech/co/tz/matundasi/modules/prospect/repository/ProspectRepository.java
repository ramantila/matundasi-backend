package bytech.co.tz.matundasi.modules.prospect.repository;

import bytech.co.tz.matundasi.modules.prospect.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, UUID> {
    List<Prospect> findAllByDeletedAtIsNullOrderByNameAsc();

    Optional<Prospect> findByIdAndDeletedAtIsNull(UUID id);
}
