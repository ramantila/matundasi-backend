package bytech.co.tz.matundasi.modules.elements.service;

import bytech.co.tz.matundasi.modules.elements.entity.Element;
import bytech.co.tz.matundasi.modules.elements.payload.ElementRequests;
import bytech.co.tz.matundasi.modules.elements.repository.ElementRepository;
import bytech.co.tz.matundasi.modules.elements.response.ElementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElementService {
    private final ElementRepository elementRepository;

    @Transactional
    public ElementResponse create(ElementRequests request) {
        String elementName = request.elementName().trim();
        String symbol = request.symbol().trim();

        if (elementRepository.existsByElementNameIgnoreCaseAndDeletedAtIsNull(elementName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Element name already exists");
        }
        if (elementRepository.existsBySymbolIgnoreCaseAndDeletedAtIsNull(symbol)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Element symbol already exists");
        }

        Element element = Element.builder()
                .elementName(elementName)
                .symbol(symbol)
                .build();

        return toResponse(elementRepository.save(element));
    }

    @Transactional(readOnly = true)
    public List<ElementResponse> findAll() {
        return elementRepository.findAllByDeletedAtIsNullOrderByElementNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ElementResponse findById(UUID id) {
        return toResponse(getActiveElement(id));
    }

    @Transactional
    public ElementResponse update(UUID id, ElementRequests request) {
        Element element = getActiveElement(id);
        String elementName = request.elementName().trim();
        String symbol = request.symbol().trim();

        if (elementRepository.existsByElementNameIgnoreCaseAndDeletedAtIsNullAndIdNot(elementName, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Element name already exists");
        }
        if (elementRepository.existsBySymbolIgnoreCaseAndDeletedAtIsNullAndIdNot(symbol, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Element symbol already exists");
        }

        element.setElementName(elementName);
        element.setSymbol(symbol);

        return toResponse(elementRepository.save(element));
    }

    @Transactional
    public void delete(UUID id) {
        Element element = getActiveElement(id);
        element.softDelete();
        elementRepository.save(element);
    }

    private Element getActiveElement(UUID id) {
        return elementRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Element not found"));
    }

    private ElementResponse toResponse(Element element) {
        return new ElementResponse(
                element.getId(),
                element.getElementName(),
                element.getSymbol()
        );
    }
}
