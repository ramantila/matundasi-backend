package bytech.co.tz.matundasi.modules.mw_s.repository;

import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MwsRepository extends JpaRepository<Mws, UUID> {
    List<Mws> findAllByDeletedAtIsNullOrderByHoleAscHdepthAsc();

    Optional<Mws> findByIdAndDeletedAtIsNull(UUID id);
}
