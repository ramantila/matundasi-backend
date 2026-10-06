package bytech.co.tz.matundasi.modules.drill.service;

import bytech.co.tz.matundasi.modules.drill.entity.Drill;
import bytech.co.tz.matundasi.modules.drill.payload.DrillRequests;
import bytech.co.tz.matundasi.modules.drill.repository.DrillRepository;
import bytech.co.tz.matundasi.modules.drill.response.DrillResponse;
import bytech.co.tz.matundasi.modules.elements.entity.Element;
import bytech.co.tz.matundasi.modules.elements.repository.ElementRepository;
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
public class DrillService {
    private final DrillRepository drillRepository;
    private final MwsRepository mwsRepository;
    private final ElementRepository elementRepository;

    @Transactional
    public DrillResponse create(DrillRequests request) {
        validateDepths(request);

        Drill drill = new Drill();
        apply(drill, request);

        return toResponse(drillRepository.save(drill));
    }

    @Transactional(readOnly = true)
    public List<DrillResponse> findAll() {
        return drillRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DrillResponse findById(UUID id) {
        return toResponse(getActiveDrill(id));
    }

    @Transactional
    public DrillResponse update(UUID id, DrillRequests request) {
        Drill drill = getActiveDrill(id);
        validateDepths(request);

        apply(drill, request);

        return toResponse(drillRepository.save(drill));
    }

    @Transactional
    public void delete(UUID id) {
        Drill drill = getActiveDrill(id);
        drill.softDelete();
        drillRepository.save(drill);
    }

    private void apply(Drill drill, DrillRequests request) {
        drill.setOrderNumber(request.orderNumber());
        drill.setMws(getActiveMws(request.mwsId()));
        drill.setSample(request.sample().trim());
        drill.setFromDepth(request.fromDepth());
        drill.setToDepth(request.toDepth());
        drill.setFlag(trimToNull(request.flag()));
        drill.setFlagId(trimToNull(request.flagId()));
        drill.setExclude(Boolean.TRUE.equals(request.exclude()));
        drill.setRiffleGrab(request.riffleGrab());
        drill.setRecoveryPercent(request.recoveryPercent());
        drill.setWetDry(request.wetDry());
        drill.setAu(request.au());
        drill.setAuRp1(request.auRp1());
        drill.setAuRp2(request.auRp2());
        drill.setAuRp3(request.auRp3());
        drill.setAuRp4(request.auRp4());
        drill.setAu2(request.au2());
        drill.setAu2Rp1(request.au2Rp1());
        drill.setAu2Plot(request.au2Plot());
        drill.setComments(trimToNull(request.comments()));
        drill.setElement(getActiveElement(request.elementId()));
    }

    private void validateDepths(DrillRequests request) {
        if (request.toDepth().compareTo(request.fromDepth()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to_depth must be greater than from_depth");
        }
    }

    private Drill getActiveDrill(UUID id) {
        return drillRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drill record not found"));
    }

    private Element getActiveElement(UUID elementId) {
        return elementRepository.findByIdAndDeletedAtIsNull(elementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Element not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private DrillResponse toResponse(Drill drill) {
        return new DrillResponse(
                drill.getId(),
                drill.getOrderNumber(),
                drill.getMws().getId(),
                drill.getMws().getHole(),
                drill.getSample(),
                drill.getFromDepth(),
                drill.getToDepth(),
                drill.getFlag(),
                drill.getFlagId(),
                drill.getExclude(),
                drill.getRiffleGrab(),
                drill.getRecoveryPercent(),
                drill.getWetDry(),
                drill.getAu(),
                drill.getAuRp1(),
                drill.getAuRp2(),
                drill.getAuRp3(),
                drill.getAuRp4(),
                drill.getAu2(),
                drill.getAu2Rp1(),
                drill.getAu2Plot(),
                drill.getComments(),
                drill.getElement().getId()
        );
    }
}
