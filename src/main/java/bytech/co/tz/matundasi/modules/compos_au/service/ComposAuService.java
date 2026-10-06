package bytech.co.tz.matundasi.modules.compos_au.service;

import bytech.co.tz.matundasi.modules.compos_au.entity.ComposAu;
import bytech.co.tz.matundasi.modules.compos_au.payload.ComposAuRequests;
import bytech.co.tz.matundasi.modules.compos_au.repository.ComposAuRepository;
import bytech.co.tz.matundasi.modules.compos_au.response.ComposAuResponse;
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
public class ComposAuService {
    private final ComposAuRepository composAuRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public ComposAuResponse create(ComposAuRequests request) {
        validateDepths(request);

        if (composAuRepository.existsByMwsIdAndFromDepthAndDeletedAtIsNull(request.mwsId(), request.fromDepth())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "An Au composite already exists for this hole at this from_depth");
        }

        ComposAu composAu = new ComposAu();
        apply(composAu, request);

        return toResponse(composAuRepository.save(composAu));
    }

    @Transactional(readOnly = true)
    public List<ComposAuResponse> findAll() {
        return composAuRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscFromDepthAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ComposAuResponse findById(UUID id) {
        return toResponse(getActiveComposAu(id));
    }

    @Transactional
    public ComposAuResponse update(UUID id, ComposAuRequests request) {
        ComposAu composAu = getActiveComposAu(id);
        validateDepths(request);

        if (composAuRepository.existsByMwsIdAndFromDepthAndDeletedAtIsNullAndIdNot(request.mwsId(), request.fromDepth(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "An Au composite already exists for this hole at this from_depth");
        }

        apply(composAu, request);

        return toResponse(composAuRepository.save(composAu));
    }

    @Transactional
    public void delete(UUID id) {
        ComposAu composAu = getActiveComposAu(id);
        composAu.softDelete();
        composAuRepository.save(composAu);
    }

    private void apply(ComposAu composAu, ComposAuRequests request) {
        composAu.setMws(getActiveMws(request.mwsId()));
        composAu.setSample(request.sample().trim());
        composAu.setFromDepth(request.fromDepth());
        composAu.setToDepth(request.toDepth());
        composAu.setLength(request.toDepth().subtract(request.fromDepth()));
        composAu.setAu(request.au());
        composAu.setExclude(Boolean.TRUE.equals(request.exclude()));
        composAu.setFlag(trimToNull(request.flag()));
    }

    private void validateDepths(ComposAuRequests request) {
        if (request.toDepth().compareTo(request.fromDepth()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to_depth must be greater than from_depth");
        }
    }

    private ComposAu getActiveComposAu(UUID id) {
        return composAuRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Au composite not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    public ComposAuResponse toResponse(ComposAu composAu) {
        return new ComposAuResponse(
                composAu.getId(),
                composAu.getMws().getId(),
                composAu.getMws().getHole(),
                composAu.getSample(),
                composAu.getFromDepth(),
                composAu.getToDepth(),
                composAu.getLength(),
                composAu.getAu(),
                composAu.getExclude(),
                composAu.getFlag()
        );
    }
}
