package bytech.co.tz.matundasi.modules.rab_collar.service;

import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import bytech.co.tz.matundasi.modules.mw_s.repository.MwsRepository;
import bytech.co.tz.matundasi.modules.rab_collar.entity.RabCollar;
import bytech.co.tz.matundasi.modules.rab_collar.payload.RabCollarRequests;
import bytech.co.tz.matundasi.modules.rab_collar.repository.RabCollarRepository;
import bytech.co.tz.matundasi.modules.rab_collar.response.RabCollarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RabCollarService {
    private final RabCollarRepository rabCollarRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public RabCollarResponse create(RabCollarRequests request) {
        if (rabCollarRepository.existsByMwsIdAndDeletedAtIsNull(request.mwsId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "RAB collar for this hole already exists");
        }

        RabCollar rabCollar = new RabCollar();
        apply(rabCollar, request);

        return toResponse(rabCollarRepository.save(rabCollar));
    }

    @Transactional(readOnly = true)
    public List<RabCollarResponse> findAll() {
        return rabCollarRepository.findAllByDeletedAtIsNullOrderByMwsHoleAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RabCollarResponse findById(UUID id) {
        return toResponse(getActiveRabCollar(id));
    }

    @Transactional
    public RabCollarResponse update(UUID id, RabCollarRequests request) {
        RabCollar rabCollar = getActiveRabCollar(id);
        if (rabCollarRepository.existsByMwsIdAndDeletedAtIsNullAndIdNot(request.mwsId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "RAB collar for this hole already exists");
        }

        apply(rabCollar, request);

        return toResponse(rabCollarRepository.save(rabCollar));
    }

    @Transactional
    public void delete(UUID id) {
        RabCollar rabCollar = getActiveRabCollar(id);
        rabCollar.softDelete();
        rabCollarRepository.save(rabCollar);
    }

    private void apply(RabCollar rabCollar, RabCollarRequests request) {
        rabCollar.setMws(getActiveMws(request.mwsId()));
        rabCollar.setType(trimToNull(request.type()));
        rabCollar.setUtmE(request.utmE());
        rabCollar.setUtmN(request.utmN());
        rabCollar.setHdepth(request.hdepth());
    }

    private RabCollar getActiveRabCollar(UUID id) {
        return rabCollarRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RAB collar not found"));
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public RabCollarResponse toResponse(RabCollar rabCollar) {
        return new RabCollarResponse(
                rabCollar.getId(),
                rabCollar.getMws().getId(),
                rabCollar.getMws().getHole(),
                rabCollar.getType(),
                rabCollar.getUtmE(),
                rabCollar.getUtmN(),
                rabCollar.getHdepth()
        );
    }
}
