package bytech.co.tz.matundasi.modules.composite.service;

import bytech.co.tz.matundasi.modules.composite.entity.Composite;
import bytech.co.tz.matundasi.modules.composite.payload.CompositeRequests;
import bytech.co.tz.matundasi.modules.composite.repository.CompositeRepository;
import bytech.co.tz.matundasi.modules.composite.response.CompositeResponse;
import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import bytech.co.tz.matundasi.modules.mw_s.repository.MwsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompositeService {
    private final CompositeRepository compositeRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public CompositeResponse create(CompositeRequests request) {
        validateDepths(request);

        if (compositeRepository.existsByMwsIdAndFromDepthAndDeletedAtIsNull(request.mwsId(), request.fromDepth())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A composite already exists for this hole at this from_depth");
        }

        Composite composite = new Composite();
        apply(composite, request);

        return toResponse(compositeRepository.save(composite));
    }

    @Transactional(readOnly = true)
    public List<CompositeResponse> findAll() {
        return compositeRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscFromDepthAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompositeResponse findById(UUID id) {
        return toResponse(getActiveComposite(id));
    }

    @Transactional
    public CompositeResponse update(UUID id, CompositeRequests request) {
        Composite composite = getActiveComposite(id);
        validateDepths(request);

        if (compositeRepository.existsByMwsIdAndFromDepthAndDeletedAtIsNullAndIdNot(request.mwsId(), request.fromDepth(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A composite already exists for this hole at this from_depth");
        }

        apply(composite, request);

        return toResponse(compositeRepository.save(composite));
    }

    @Transactional
    public void delete(UUID id) {
        Composite composite = getActiveComposite(id);
        composite.softDelete();
        compositeRepository.save(composite);
    }

    private void apply(Composite composite, CompositeRequests request) {
        composite.setMws(getActiveMws(request.mwsId()));
        composite.setFromDepth(request.fromDepth());
        composite.setToDepth(request.toDepth());
        composite.setLength(request.toDepth().subtract(request.fromDepth()));
        composite.setAverageNi(request.averageNi());
        composite.setAverageCo(request.averageCo());
    }

    private void validateDepths(CompositeRequests request) {
        if (request.toDepth().compareTo(request.fromDepth()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to_depth must be greater than from_depth");
        }
    }

    private Composite getActiveComposite(UUID id) {
        return compositeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Composite not found"));
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private CompositeResponse toResponse(Composite composite) {
        return new CompositeResponse(
                composite.getId(),
                composite.getMws().getId(),
                composite.getMws().getHole(),
                composite.getFromDepth(),
                composite.getToDepth(),
                composite.getLength(),
                composite.getAverageNi(),
                composite.getAverageCo()
        );
    }
}
