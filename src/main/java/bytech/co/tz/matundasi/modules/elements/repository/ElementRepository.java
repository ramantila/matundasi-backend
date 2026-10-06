package bytech.co.tz.matundasi.modules.elements.repository;

import bytech.co.tz.matundasi.modules.elements.entity.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElementRepository extends JpaRepository<Element, UUID> {
    List<Element> findAllByDeletedAtIsNullOrderByElementNameAsc();

    Optional<Element> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByElementNameIgnoreCaseAndDeletedAtIsNull(String elementName);

    boolean existsBySymbolIgnoreCaseAndDeletedAtIsNull(String symbol);

    boolean existsByElementNameIgnoreCaseAndDeletedAtIsNullAndIdNot(String elementName, UUID id);

    boolean existsBySymbolIgnoreCaseAndDeletedAtIsNullAndIdNot(String symbol, UUID id);
}
