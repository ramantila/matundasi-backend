package bytech.co.tz.matundasi.modules.colours.repository;

import bytech.co.tz.matundasi.modules.colours.entity.Colour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColourRepository extends JpaRepository<Colour, UUID> {
    List<Colour> findAllByDeletedAtIsNullOrderByColourNameAsc();

    Optional<Colour> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByColourCodeIgnoreCaseAndDeletedAtIsNull(String colourCode);

    boolean existsByColourNameIgnoreCaseAndDeletedAtIsNull(String colourName);

    boolean existsByColourCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(String colourCode, UUID id);

    boolean existsByColourNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String colourName, UUID id);
}
