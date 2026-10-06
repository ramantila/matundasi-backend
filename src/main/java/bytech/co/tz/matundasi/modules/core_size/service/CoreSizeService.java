package bytech.co.tz.matundasi.modules.core_size.service;

import bytech.co.tz.matundasi.modules.core_size.entity.CoreSize;
import bytech.co.tz.matundasi.modules.core_size.payload.CoreSizeRequests;
import bytech.co.tz.matundasi.modules.core_size.repository.CoreSizeRepository;
import bytech.co.tz.matundasi.modules.core_size.response.CoreSizeResponse;
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
public class CoreSizeService {
    private final CoreSizeRepository coreSizeRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public CoreSizeResponse create(CoreSizeRequests request) {
        validateDepths(request);
        if (coreSizeRepository.existsByMwsIdAndCoreFromAndDeletedAtIsNull(request.mwsId(), request.coreFrom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A core size interval already exists for this hole at this core_from");
        }

        CoreSize coreSize = new CoreSize();
        apply(coreSize, request);

        return toResponse(coreSizeRepository.save(coreSize));
    }

    @Transactional(readOnly = true)
    public List<CoreSizeResponse> findAll() {
        return coreSizeRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscCoreFromAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CoreSizeResponse findById(UUID id) {
        return toResponse(getActiveCoreSize(id));
    }

    @Transactional
    public CoreSizeResponse update(UUID id, CoreSizeRequests request) {
        CoreSize coreSize = getActiveCoreSize(id);
        validateDepths(request);
        if (coreSizeRepository.existsByMwsIdAndCoreFromAndDeletedAtIsNullAndIdNot(
                request.mwsId(), request.coreFrom(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A core size interval already exists for this hole at this core_from");
        }

        apply(coreSize, request);

        return toResponse(coreSizeRepository.save(coreSize));
    }

    @Transactional
    public void delete(UUID id) {
        CoreSize coreSize = getActiveCoreSize(id);
        coreSize.softDelete();
        coreSizeRepository.save(coreSize);
    }

    private void apply(CoreSize coreSize, CoreSizeRequests request) {
        coreSize.setMws(getActiveMws(request.mwsId()));
        coreSize.setPreCollarTo(request.preCollarTo());
        coreSize.setPreCollarMethod(trimToNull(request.preCollarMethod()));
        coreSize.setCoreFrom(request.coreFrom());
        coreSize.setCoreTo(request.coreTo());
        coreSize.setCoreSize(request.coreSize().trim());
    }

    private void validateDepths(CoreSizeRequests request) {
        if (request.coreTo().compareTo(request.coreFrom()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "core_to must be greater than core_from");
        }
    }

    private CoreSize getActiveCoreSize(UUID id) {
        return coreSizeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Core size not found"));
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private CoreSizeResponse toResponse(CoreSize coreSize) {
        return new CoreSizeResponse(
                coreSize.getId(),
                coreSize.getMws().getId(),
                coreSize.getMws().getHole(),
                coreSize.getPreCollarTo(),
                coreSize.getPreCollarMethod(),
                coreSize.getCoreFrom(),
                coreSize.getCoreTo(),
                coreSize.getCoreSize()
        );
    }
}
