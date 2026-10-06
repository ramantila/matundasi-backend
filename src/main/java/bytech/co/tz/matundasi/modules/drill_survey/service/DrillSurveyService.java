package bytech.co.tz.matundasi.modules.drill_survey.service;

import bytech.co.tz.matundasi.modules.drill_survey.entity.DrillSurvey;
import bytech.co.tz.matundasi.modules.drill_survey.payload.DrillSurveyRequests;
import bytech.co.tz.matundasi.modules.drill_survey.repository.DrillSurveyRepository;
import bytech.co.tz.matundasi.modules.drill_survey.response.DrillSurveyResponse;
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
public class DrillSurveyService {
    private final DrillSurveyRepository drillSurveyRepository;
    private final MwsRepository mwsRepository;

    @Transactional
    public DrillSurveyResponse create(DrillSurveyRequests request) {
        if (drillSurveyRepository.existsByMwsIdAndDowndepthAndDeletedAtIsNull(request.mwsId(), request.downdepth())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A survey already exists for this hole at this downdepth");
        }

        DrillSurvey drillSurvey = DrillSurvey.builder()
                .mws(getActiveMws(request.mwsId()))
                .downdepth(request.downdepth())
                .dip(request.dip())
                .azimuth(request.azimuth())
                .utmAz(request.utmAz())
                .build();

        return toResponse(drillSurveyRepository.save(drillSurvey));
    }

    @Transactional(readOnly = true)
    public List<DrillSurveyResponse> findAll() {
        return drillSurveyRepository.findAllByDeletedAtIsNullOrderByMwsHoleAscDowndepthAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DrillSurveyResponse findById(UUID id) {
        return toResponse(getActiveDrillSurvey(id));
    }

    @Transactional
    public DrillSurveyResponse update(UUID id, DrillSurveyRequests request) {
        DrillSurvey drillSurvey = getActiveDrillSurvey(id);
        if (drillSurveyRepository.existsByMwsIdAndDowndepthAndDeletedAtIsNullAndIdNot(
                request.mwsId(), request.downdepth(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A survey already exists for this hole at this downdepth");
        }

        drillSurvey.setMws(getActiveMws(request.mwsId()));
        drillSurvey.setDowndepth(request.downdepth());
        drillSurvey.setDip(request.dip());
        drillSurvey.setAzimuth(request.azimuth());
        drillSurvey.setUtmAz(request.utmAz());

        return toResponse(drillSurveyRepository.save(drillSurvey));
    }

    @Transactional
    public void delete(UUID id) {
        DrillSurvey drillSurvey = getActiveDrillSurvey(id);
        drillSurvey.softDelete();
        drillSurveyRepository.save(drillSurvey);
    }

    private DrillSurvey getActiveDrillSurvey(UUID id) {
        return drillSurveyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drill survey not found"));
    }

    private Mws getActiveMws(UUID mwsId) {
        return mwsRepository.findByIdAndDeletedAtIsNull(mwsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private DrillSurveyResponse toResponse(DrillSurvey drillSurvey) {
        return new DrillSurveyResponse(
                drillSurvey.getId(),
                drillSurvey.getMws().getId(),
                drillSurvey.getMws().getHole(),
                drillSurvey.getDowndepth(),
                drillSurvey.getDip(),
                drillSurvey.getAzimuth(),
                drillSurvey.getUtmAz()
        );
    }
}
