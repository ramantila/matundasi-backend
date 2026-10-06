package bytech.co.tz.matundasi.modules.per_area_hole.service;

import bytech.co.tz.matundasi.modules.per_area_hole.entity.PerAreaHole;
import bytech.co.tz.matundasi.modules.per_area_hole.payload.PerAreaHoleRequests;
import bytech.co.tz.matundasi.modules.per_area_hole.repository.PerAreaHoleRepository;
import bytech.co.tz.matundasi.modules.per_area_hole.response.PerAreaHoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerAreaHoleService {
    private final PerAreaHoleRepository perAreaHoleRepository;

    @Transactional
    public PerAreaHoleResponse create(PerAreaHoleRequests request) {
        validate(request);

        PerAreaHole perAreaHole = new PerAreaHole();
        apply(perAreaHole, request);

        return toResponse(perAreaHoleRepository.save(perAreaHole));
    }

    @Transactional(readOnly = true)
    public List<PerAreaHoleResponse> findAll() {
        return perAreaHoleRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PerAreaHoleResponse findById(UUID id) {
        return toResponse(getActivePerAreaHole(id));
    }

    @Transactional
    public PerAreaHoleResponse update(UUID id, PerAreaHoleRequests request) {
        PerAreaHole perAreaHole = getActivePerAreaHole(id);
        validate(request);

        apply(perAreaHole, request);

        return toResponse(perAreaHoleRepository.save(perAreaHole));
    }

    @Transactional
    public void delete(UUID id) {
        PerAreaHole perAreaHole = getActivePerAreaHole(id);
        perAreaHole.softDelete();
        perAreaHoleRepository.save(perAreaHole);
    }

    private void apply(PerAreaHole perAreaHole, PerAreaHoleRequests request) {
        perAreaHole.setBuhDiamond(trimToNull(request.buhDiamond()));
        perAreaHole.setBuhRab(trimToNull(request.buhRab()));
        perAreaHole.setBuhRc(trimToNull(request.buhRc()));
        perAreaHole.setKilamoDiamond(trimToNull(request.kilamoDiamond()));
        perAreaHole.setKilamoRab(trimToNull(request.kilamoRab()));
        perAreaHole.setKilamoRc(trimToNull(request.kilamoRc()));
        perAreaHole.setNyaseRab(trimToNull(request.nyaseRab()));
        perAreaHole.setNyaseRc(trimToNull(request.nyaseRc()));
        perAreaHole.setNyaseDdh(trimToNull(request.nyaseDdh()));
    }

    private void validate(PerAreaHoleRequests request) {
        if (isBlank(request.buhDiamond()) && isBlank(request.buhRab()) && isBlank(request.buhRc()) &&
                isBlank(request.kilamoDiamond()) && isBlank(request.kilamoRab()) &&
                isBlank(request.kilamoRc()) && isBlank(request.nyaseRab()) && isBlank(request.nyaseRc()) &&
                isBlank(request.nyaseDdh())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one hole must be provided");
        }
    }

    private PerAreaHole getActivePerAreaHole(UUID id) {
        return perAreaHoleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Per area hole record not found"));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String trimToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    public PerAreaHoleResponse toResponse(PerAreaHole perAreaHole) {
        return new PerAreaHoleResponse(
                perAreaHole.getId(),
                perAreaHole.getBuhDiamond(),
                perAreaHole.getBuhRab(),
                perAreaHole.getBuhRc(),
                perAreaHole.getKilamoDiamond(),
                perAreaHole.getKilamoRab(),
                perAreaHole.getKilamoRc(),
                perAreaHole.getNyaseRab(),
                perAreaHole.getNyaseRc(),
                perAreaHole.getNyaseDdh()
        );
    }
}
