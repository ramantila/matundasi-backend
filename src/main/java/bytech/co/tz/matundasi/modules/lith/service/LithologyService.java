package bytech.co.tz.matundasi.modules.lith.service;

import bytech.co.tz.matundasi.modules.lith.entity.Lithology;
import bytech.co.tz.matundasi.modules.lith.payload.LithologyRequests;
import bytech.co.tz.matundasi.modules.lith.repository.LithologyRepository;
import bytech.co.tz.matundasi.modules.lith.response.LithologyResponse;
import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import bytech.co.tz.matundasi.modules.mw_s.repository.MwsRepository;
import bytech.co.tz.matundasi.modules.weath.entity.Weathering;
import bytech.co.tz.matundasi.modules.weath.repository.WeatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LithologyService {
    private final LithologyRepository lithologyRepository;
    private final MwsRepository mwsRepository;
    private final WeatheringRepository weatheringRepository;

    @Transactional
    public LithologyResponse create(LithologyRequests request) {
        validateDepths(request);

        if (lithologyRepository.existsByMwsIdAndLithfromAndDeletedAtIsNull(request.mwsId(), request.lithfrom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A lithology interval already exists for this hole at this lithfrom");
        }

        Lithology lithology = new Lithology();
        apply(lithology, request);

        return toResponse(lithologyRepository.save(lithology));
    }

    @Transactional(readOnly = true)
    public List<LithologyResponse> findAll() {
        return lithologyRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscLithfromAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LithologyResponse findById(UUID id) {
        return toResponse(getActiveLithology(id));
    }

    @Transactional
    public LithologyResponse update(UUID id, LithologyRequests request) {
        Lithology lithology = getActiveLithology(id);
        validateDepths(request);

        if (lithologyRepository.existsByMwsIdAndLithfromAndDeletedAtIsNullAndIdNot(request.mwsId(), request.lithfrom(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A lithology interval already exists for this hole at this lithfrom");
        }

        apply(lithology, request);

        return toResponse(lithologyRepository.save(lithology));
    }

    @Transactional
    public void delete(UUID id) {
        Lithology lithology = getActiveLithology(id);
        lithology.softDelete();
        lithologyRepository.save(lithology);
    }

    private void apply(Lithology lithology, LithologyRequests request) {
        lithology.setMws(getActiveMws(request.mwsId()));
        lithology.setLithfrom(request.lithfrom());
        lithology.setLithto(request.lithto());
        lithology.setLithcode(request.lithcode().trim());
        lithology.setQuartzPercent(request.quartzPercent());
        lithology.setPyritesPercent(request.pyritesPercent());
        lithology.setAlteration(trimToNull(request.alteration()));
        lithology.setSchistosity(trimToNull(request.schistosity()));
        lithology.setGrainsize(trimToNull(request.grainsize()));
        lithology.setColour(trimToNull(request.colour()));
        lithology.setWeathcode(request.weathcodeId() == null ? null : getActiveWeathering(request.weathcodeId()));
        lithology.setComments(trimToNull(request.comments()));
    }

    private void validateDepths(LithologyRequests request) {
        if (request.lithto().compareTo(request.lithfrom()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lithto must be greater than lithfrom");
        }
    }

    private Lithology getActiveLithology(UUID id) {
        return lithologyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lithology not found"));
    }

    private Weathering getActiveWeathering(UUID weatheringId) {
        return weatheringRepository.findByIdAndDeletedAtIsNull(weatheringId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Weathering code not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    public LithologyResponse toResponse(Lithology lithology) {
        return new LithologyResponse(
                lithology.getId(),
                lithology.getMws().getId(),
                lithology.getMws().getHole(),
                lithology.getLithfrom(),
                lithology.getLithto(),
                lithology.getLithcode(),
                lithology.getQuartzPercent(),
                lithology.getPyritesPercent(),
                lithology.getAlteration(),
                lithology.getSchistosity(),
                lithology.getGrainsize(),
                lithology.getColour(),
                lithology.getWeathcode() == null ? null : lithology.getWeathcode().getId(),
                lithology.getComments()
        );
    }
}
