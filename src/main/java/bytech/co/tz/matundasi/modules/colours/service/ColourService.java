package bytech.co.tz.matundasi.modules.colours.service;

import bytech.co.tz.matundasi.modules.colours.entity.Colour;
import bytech.co.tz.matundasi.modules.colours.payload.ColourRequests;
import bytech.co.tz.matundasi.modules.colours.repository.ColourRepository;
import bytech.co.tz.matundasi.modules.colours.response.ColourResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ColourService {
    private final ColourRepository colourRepository;

    @Transactional
    public ColourResponse create(ColourRequests request) {
        String colourCode = request.colourCode().trim();
        String colourName = request.colourName().trim();

        if (colourRepository.existsByColourCodeIgnoreCaseAndDeletedAtIsNull(colourCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Colour code already exists");
        }
        if (colourRepository.existsByColourNameIgnoreCaseAndDeletedAtIsNull(colourName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Colour name already exists");
        }

        Colour colour = Colour.builder()
                .colourCode(colourCode)
                .colourName(colourName)
                .build();

        return toResponse(colourRepository.save(colour));
    }

    @Transactional(readOnly = true)
    public List<ColourResponse> findAll() {
        return colourRepository.findAllByDeletedAtIsNullOrderByColourNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ColourResponse findById(UUID id) {
        return toResponse(getActiveColour(id));
    }

    @Transactional
    public ColourResponse update(UUID id, ColourRequests request) {
        Colour colour = getActiveColour(id);
        String colourCode = request.colourCode().trim();
        String colourName = request.colourName().trim();

        if (colourRepository.existsByColourCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(colourCode, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Colour code already exists");
        }
        if (colourRepository.existsByColourNameIgnoreCaseAndDeletedAtIsNullAndIdNot(colourName, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Colour name already exists");
        }

        colour.setColourCode(colourCode);
        colour.setColourName(colourName);

        return toResponse(colourRepository.save(colour));
    }

    @Transactional
    public void delete(UUID id) {
        Colour colour = getActiveColour(id);
        colour.softDelete();
        colourRepository.save(colour);
    }

    private Colour getActiveColour(UUID id) {
        return colourRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colour not found"));
    }

    private ColourResponse toResponse(Colour colour) {
        return new ColourResponse(
                colour.getId(),
                colour.getColourCode(),
                colour.getColourName()
        );
    }
}
