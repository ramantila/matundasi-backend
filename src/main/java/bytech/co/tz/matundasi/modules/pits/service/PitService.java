package bytech.co.tz.matundasi.modules.pits.service;

import bytech.co.tz.matundasi.modules.lith.entity.Lithology;
import bytech.co.tz.matundasi.modules.lith.repository.LithologyRepository;
import bytech.co.tz.matundasi.modules.pits.entity.Pit;
import bytech.co.tz.matundasi.modules.pits.payload.PitRequests;
import bytech.co.tz.matundasi.modules.pits.repository.PitRepository;
import bytech.co.tz.matundasi.modules.pits.response.PitResponse;
import bytech.co.tz.matundasi.modules.sample.entity.Sample;
import bytech.co.tz.matundasi.modules.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PitService {
    private final PitRepository pitRepository;
    private final LithologyRepository lithologyRepository;
    private final SampleRepository sampleRepository;

    @Transactional
    public PitResponse create(PitRequests request) {
        validateDepths(request);

        Pit pit = new Pit();
        apply(pit, request);

        return toResponse(pitRepository.save(pit));
    }

    @Transactional(readOnly = true)
    public List<PitResponse> findAll() {
        return pitRepository.findAllByDeletedAtIsNullOrderByPitNumberAscOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PitResponse findById(UUID id) {
        return toResponse(getActivePit(id));
    }

    @Transactional
    public PitResponse update(UUID id, PitRequests request) {
        Pit pit = getActivePit(id);
        validateDepths(request);

        apply(pit, request);

        return toResponse(pitRepository.save(pit));
    }

    @Transactional
    public void delete(UUID id) {
        Pit pit = getActivePit(id);
        pit.softDelete();
        pitRepository.save(pit);
    }

    private void apply(Pit pit, PitRequests request) {
        pit.setOrderNumber(request.orderNumber());
        pit.setPitNumber(request.pitNumber().trim());
        pit.setLocalE(request.localE());
        pit.setLocalN(request.localN());
        pit.setUtmE(request.utmE());
        pit.setUtmN(request.utmN());
        pit.setRlM(request.rlM());
        pit.setFromDepth(request.fromDepth());
        pit.setToDepth(request.toDepth());
        pit.setLithology(request.lithologyId() == null ? null : getActiveLithology(request.lithologyId()));
        pit.setFraction(trimToNull(request.fraction()));
        pit.setSample(request.sampleId() == null ? null : getActiveSample(request.sampleId()));
        pit.setAuAvg(request.auAvg());
        pit.setAuPpb(request.auPpb());
        pit.setAuRpt1(request.auRpt1());
        pit.setAuRpt2(request.auRpt2());
        pit.setLaboratory(trimToNull(request.laboratory()));
        pit.setComments(trimToNull(request.comments()));
        pit.setStudyType(trimToNull(request.studyType()));
    }

    private void validateDepths(PitRequests request) {
        if (request.toDepth().compareTo(request.fromDepth()) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to_depth must be greater than from_depth");
        }
    }

    private Pit getActivePit(UUID id) {
        return pitRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pit not found"));
    }

    private Lithology getActiveLithology(UUID lithologyId) {
        return lithologyRepository.findByIdAndDeletedAtIsNull(lithologyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lithology not found"));
    }

    private Sample getActiveSample(UUID sampleId) {
        return sampleRepository.findByIdAndDeletedAtIsNull(sampleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sample not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private PitResponse toResponse(Pit pit) {
        return new PitResponse(
                pit.getId(),
                pit.getOrderNumber(),
                pit.getPitNumber(),
                pit.getLocalE(),
                pit.getLocalN(),
                pit.getUtmE(),
                pit.getUtmN(),
                pit.getRlM(),
                pit.getFromDepth(),
                pit.getToDepth(),
                pit.getLithology() == null ? null : pit.getLithology().getId(),
                pit.getFraction(),
                pit.getSample() == null ? null : pit.getSample().getId(),
                pit.getAuAvg(),
                pit.getAuPpb(),
                pit.getAuRpt1(),
                pit.getAuRpt2(),
                pit.getLaboratory(),
                pit.getComments(),
                pit.getStudyType()
        );
    }
}
