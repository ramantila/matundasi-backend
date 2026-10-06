package bytech.co.tz.matundasi.modules.rocks.service;

import bytech.co.tz.matundasi.modules.rocks.entity.Rock;
import bytech.co.tz.matundasi.modules.rocks.payload.RockRequests;
import bytech.co.tz.matundasi.modules.rocks.repository.RockRepository;
import bytech.co.tz.matundasi.modules.rocks.response.RockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RockService {
    private final RockRepository rockRepository;

    @Transactional
    public RockResponse create(RockRequests request) {
        String rockCode = request.rockCode().trim();

        if (rockRepository.existsByRockCodeIgnoreCaseAndDeletedAtIsNull(rockCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rock code already exists");
        }

        Rock rock = Rock.builder()
                .rockCode(rockCode)
                .description(trimToNull(request.description()))
                .regime(request.regime().trim())
                .build();

        return toResponse(rockRepository.save(rock));
    }

    @Transactional(readOnly = true)
    public List<RockResponse> findAll() {
        return rockRepository.findAllByDeletedAtIsNullOrderByRockCodeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RockResponse findById(UUID id) {
        return toResponse(getActiveRock(id));
    }

    @Transactional
    public RockResponse update(UUID id, RockRequests request) {
        Rock rock = getActiveRock(id);
        String rockCode = request.rockCode().trim();

        if (rockRepository.existsByRockCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(rockCode, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rock code already exists");
        }

        rock.setRockCode(rockCode);
        rock.setDescription(trimToNull(request.description()));
        rock.setRegime(request.regime().trim());

        return toResponse(rockRepository.save(rock));
    }

    @Transactional
    public void delete(UUID id) {
        Rock rock = getActiveRock(id);
        rock.softDelete();
        rockRepository.save(rock);
    }

    private Rock getActiveRock(UUID id) {
        return rockRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rock not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private RockResponse toResponse(Rock rock) {
        return new RockResponse(
                rock.getId(),
                rock.getRockCode(),
                rock.getDescription(),
                rock.getRegime()
        );
    }
}
