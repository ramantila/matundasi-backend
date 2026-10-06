package bytech.co.tz.matundasi.modules.sample.service;

import bytech.co.tz.matundasi.modules.elements.entity.Element;
import bytech.co.tz.matundasi.modules.elements.repository.ElementRepository;
import bytech.co.tz.matundasi.modules.sample.entity.Sample;
import bytech.co.tz.matundasi.modules.sample.payload.SampleRequests;
import bytech.co.tz.matundasi.modules.sample.repository.SampleRepository;
import bytech.co.tz.matundasi.modules.sample.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleRepository sampleRepository;
    private final ElementRepository elementRepository;

    @Transactional
    public SampleResponse create(SampleRequests request) {
        Sample sample = new Sample();
        apply(sample, request);

        return toResponse(sampleRepository.save(sample));
    }

    @Transactional(readOnly = true)
    public List<SampleResponse> findAll() {
        return sampleRepository.findAllByDeletedAtIsNullOrderByOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SampleResponse findById(UUID id) {
        return toResponse(getActiveSample(id));
    }

    @Transactional
    public SampleResponse update(UUID id, SampleRequests request) {
        Sample sample = getActiveSample(id);

        apply(sample, request);

        return toResponse(sampleRepository.save(sample));
    }

    @Transactional
    public void delete(UUID id) {
        Sample sample = getActiveSample(id);
        sample.softDelete();
        sampleRepository.save(sample);
    }

    private void apply(Sample sample, SampleRequests request) {
        sample.setOrderNumber(request.orderNumber());
        sample.setSample(request.sample().trim());
        sample.setFlag(trimToNull(request.flag()));
        sample.setFlagId(trimToNull(request.flagId()));
        sample.setUtmE(request.utmE());
        sample.setUtmN(request.utmN());
        sample.setZone(trimToNull(request.zone()));
        sample.setLocalX(request.localX());
        sample.setLocalY(request.localY());
        sample.setLocalName(trimToNull(request.localName()));
        sample.setTenement(trimToNull(request.tenement()));
        sample.setMap125(trimToNull(request.map125()));
        sample.setMap50(trimToNull(request.map50()));
        sample.setCode(trimToNull(request.code()));
        sample.setDescription(trimToNull(request.description()));
        sample.setAu(request.au());
        sample.setAuRp1(request.auRp1());
        sample.setAuRp2(request.auRp2());
        sample.setAu2(request.au2());
        sample.setElement(request.elementId() == null ? null : getActiveElement(request.elementId()));
        sample.setComments(trimToNull(request.comments()));
        sample.setClaim(trimToNull(request.claim()));
    }

    private Sample getActiveSample(UUID id) {
        return sampleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sample not found"));
    }

    private Element getActiveElement(UUID elementId) {
        return elementRepository.findByIdAndDeletedAtIsNull(elementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Element not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public SampleResponse toResponse(Sample sample) {
        return new SampleResponse(
                sample.getId(),
                sample.getOrderNumber(),
                sample.getSample(),
                sample.getFlag(),
                sample.getFlagId(),
                sample.getUtmE(),
                sample.getUtmN(),
                sample.getZone(),
                sample.getLocalX(),
                sample.getLocalY(),
                sample.getLocalName(),
                sample.getTenement(),
                sample.getMap125(),
                sample.getMap50(),
                sample.getCode(),
                sample.getDescription(),
                sample.getAu(),
                sample.getAuRp1(),
                sample.getAuRp2(),
                sample.getAu2(),
                sample.getElement() == null ? null : sample.getElement().getId(),
                sample.getComments(),
                sample.getClaim()
        );
    }
}
