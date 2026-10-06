package bytech.co.tz.matundasi.modules.std_reference.service;

import bytech.co.tz.matundasi.modules.std_reference.entity.StdReference;
import bytech.co.tz.matundasi.modules.std_reference.payload.StdReferenceRequests;
import bytech.co.tz.matundasi.modules.std_reference.repository.StdReferenceRepository;
import bytech.co.tz.matundasi.modules.std_reference.response.StdReferenceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StdReferenceService {
    private final StdReferenceRepository stdReferenceRepository;

    @Transactional
    public StdReferenceResponse create(StdReferenceRequests request) {
        String standard = request.standard().trim();

        if (stdReferenceRepository.existsByStandardIgnoreCaseAndDeletedAtIsNull(standard)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Standard already exists");
        }

        StdReference stdReference = new StdReference();
        apply(stdReference, standard, request);

        return toResponse(stdReferenceRepository.save(stdReference));
    }

    @Transactional(readOnly = true)
    public List<StdReferenceResponse> findAll() {
        return stdReferenceRepository.findAllByDeletedAtIsNullOrderByStandardAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StdReferenceResponse findById(UUID id) {
        return toResponse(getActiveStdReference(id));
    }

    @Transactional
    public StdReferenceResponse update(UUID id, StdReferenceRequests request) {
        StdReference stdReference = getActiveStdReference(id);
        String standard = request.standard().trim();

        if (stdReferenceRepository.existsByStandardIgnoreCaseAndDeletedAtIsNullAndIdNot(standard, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Standard already exists");
        }

        apply(stdReference, standard, request);

        return toResponse(stdReferenceRepository.save(stdReference));
    }

    @Transactional
    public void delete(UUID id) {
        StdReference stdReference = getActiveStdReference(id);
        stdReference.softDelete();
        stdReferenceRepository.save(stdReference);
    }

    private void apply(StdReference stdReference, String standard, StdReferenceRequests request) {
        stdReference.setStandard(standard);
        stdReference.setRefAu(request.refAu());
        stdReference.setAuUnits(trimToNull(request.auUnits()));
        stdReference.setN(request.n());
        stdReference.setRefAuArAas(request.refAuArAas());
        stdReference.setAuArUnits(trimToNull(request.auArUnits()));
        stdReference.setRefAuFa50(request.refAuFa50());
        stdReference.setElementsUnit(trimToNull(request.elementsUnit()));
    }

    private StdReference getActiveStdReference(UUID id) {
        return stdReferenceRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standard reference not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public StdReferenceResponse toResponse(StdReference stdReference) {
        return new StdReferenceResponse(
                stdReference.getId(),
                stdReference.getStandard(),
                stdReference.getRefAu(),
                stdReference.getAuUnits(),
                stdReference.getN(),
                stdReference.getRefAuArAas(),
                stdReference.getAuArUnits(),
                stdReference.getRefAuFa50(),
                stdReference.getElementsUnit()
        );
    }
}
