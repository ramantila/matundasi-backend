package bytech.co.tz.matundasi.modules.prospect.service;

import bytech.co.tz.matundasi.modules.prospect.entity.Prospect;
import bytech.co.tz.matundasi.modules.prospect.payload.ProspectRequests;
import bytech.co.tz.matundasi.modules.prospect.repository.ProspectRepository;
import bytech.co.tz.matundasi.modules.prospect.response.ProspectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProspectService {
    private final ProspectRepository prospectRepository;

    @Transactional
    public ProspectResponse create(ProspectRequests request) {
        validateBounds(request);

        Prospect prospect = new Prospect();
        apply(prospect, request);

        return toResponse(prospectRepository.save(prospect));
    }

    @Transactional(readOnly = true)
    public List<ProspectResponse> findAll() {
        return prospectRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProspectResponse findById(UUID id) {
        return toResponse(getActiveProspect(id));
    }

    @Transactional
    public ProspectResponse update(UUID id, ProspectRequests request) {
        Prospect prospect = getActiveProspect(id);
        validateBounds(request);

        apply(prospect, request);

        return toResponse(prospectRepository.save(prospect));
    }

    @Transactional
    public void delete(UUID id) {
        Prospect prospect = getActiveProspect(id);
        prospect.softDelete();
        prospectRepository.save(prospect);
    }

    private void apply(Prospect prospect, ProspectRequests request) {
        prospect.setName(request.name().trim());
        prospect.setLocalName(trimToNull(request.localName()));
        prospect.setMinX(request.minX());
        prospect.setMinY(request.minY());
        prospect.setMaxX(request.maxX());
        prospect.setMaxY(request.maxY());
    }

    private void validateBounds(ProspectRequests request) {
        if (isGreater(request.minX(), request.maxX())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "min_x must not be greater than max_x");
        }
        if (isGreater(request.minY(), request.maxY())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "min_y must not be greater than max_y");
        }
    }

    private boolean isGreater(BigDecimal min, BigDecimal max) {
        return min != null && max != null && min.compareTo(max) > 0;
    }

    private Prospect getActiveProspect(UUID id) {
        return prospectRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prospect not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public ProspectResponse toResponse(Prospect prospect) {
        return new ProspectResponse(
                prospect.getId(),
                prospect.getName(),
                prospect.getLocalName(),
                prospect.getMinX(),
                prospect.getMinY(),
                prospect.getMaxX(),
                prospect.getMaxY()
        );
    }
}
