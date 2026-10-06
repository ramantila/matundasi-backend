package bytech.co.tz.matundasi.modules.collar.service;

import bytech.co.tz.matundasi.modules.collar.entity.Collar;
import bytech.co.tz.matundasi.modules.collar.payload.CollarRequests;
import bytech.co.tz.matundasi.modules.collar.repository.CollarRepository;
import bytech.co.tz.matundasi.modules.collar.response.CollarResponse;
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
public class CollarService {
    private final CollarRepository collarRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public CollarResponse create(CollarRequests request) {
        if (collarRepository.existsByMwsIdAndDeletedAtIsNull(request.mwsId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Collar for this hole already exists");
        }

        Collar collar = new Collar();
        apply(collar, request);

        return toResponse(collarRepository.save(collar));
    }

    @Transactional(readOnly = true)
    public List<CollarResponse> findAll() {
        return collarRepository.findAllByDeletedAtIsNullOrderByMwsHoleAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CollarResponse findById(UUID id) {
        return toResponse(getActiveCollar(id));
    }

    @Transactional
    public CollarResponse update(UUID id, CollarRequests request) {
        Collar collar = getActiveCollar(id);

        if (collarRepository.existsByMwsIdAndDeletedAtIsNullAndIdNot(request.mwsId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Collar for this hole already exists");
        }

        apply(collar, request);

        return toResponse(collarRepository.save(collar));
    }

    @Transactional
    public void delete(UUID id) {
        Collar collar = getActiveCollar(id);
        collar.softDelete();
        collarRepository.save(collar);
    }

    private void apply(Collar collar, CollarRequests request) {
        collar.setMws(getActiveMws(request.mwsId()));
        collar.setUtmE(request.utmE());
        collar.setUtmN(request.utmN());
        collar.setZone(trimToNull(request.zone()));
        collar.setLocalX(request.localX());
        collar.setLocalY(request.localY());
        collar.setLocalName(trimToNull(request.localName()));
        collar.setMap125(trimToNull(request.map125()));
        collar.setMap50(trimToNull(request.map50()));
        collar.setTenement(trimToNull(request.tenement()));
        collar.setClaim(trimToNull(request.claim()));
        collar.setLocalMapSheet(trimToNull(request.localMapSheet()));
        collar.setRl(request.rl());
        collar.setAzimuth(request.azimuth());
        collar.setUtmAz(request.utmAz());
        collar.setHdepth(request.hdepth());
        collar.setDateDrilled(request.dateDrilled());
    }

    private Collar getActiveCollar(UUID id) {
        return collarRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collar not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    public CollarResponse toResponse(Collar collar) {
        return new CollarResponse(
                collar.getId(),
                collar.getMws().getId(),
                collar.getMws().getHole(),
                collar.getUtmE(),
                collar.getUtmN(),
                collar.getZone(),
                collar.getLocalX(),
                collar.getLocalY(),
                collar.getLocalName(),
                collar.getMap125(),
                collar.getMap50(),
                collar.getTenement(),
                collar.getClaim(),
                collar.getLocalMapSheet(),
                collar.getRl(),
                collar.getAzimuth(),
                collar.getUtmAz(),
                collar.getHdepth(),
                collar.getDateDrilled()
        );
    }
}
