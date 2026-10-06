package bytech.co.tz.matundasi.modules.qualifiers.service;

import bytech.co.tz.matundasi.modules.qualifiers.entity.Qualifier;
import bytech.co.tz.matundasi.modules.qualifiers.payload.QualifierRequests;
import bytech.co.tz.matundasi.modules.qualifiers.repository.QualifierRepository;
import bytech.co.tz.matundasi.modules.qualifiers.response.QualifierResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QualifierService {
    private final QualifierRepository qualifierRepository;

    @Transactional
    public QualifierResponse create(QualifierRequests request) {
        String qualifierCode = request.qualifierCode().trim();

        if (qualifierRepository.existsByQualifierCodeIgnoreCaseAndDeletedAtIsNull(qualifierCode)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Qualifier code already exists");
        }

        Qualifier qualifier = Qualifier.builder()
                .qualifierCode(qualifierCode)
                .description(trimToNull(request.description()))
                .build();

        return toResponse(qualifierRepository.save(qualifier));
    }

    @Transactional(readOnly = true)
    public List<QualifierResponse> findAll() {
        return qualifierRepository.findAllByDeletedAtIsNullOrderByQualifierCodeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QualifierResponse findById(UUID id) {
        return toResponse(getActiveQualifier(id));
    }

    @Transactional
    public QualifierResponse update(UUID id, QualifierRequests request) {
        Qualifier qualifier = getActiveQualifier(id);
        String qualifierCode = request.qualifierCode().trim();

        if (qualifierRepository.existsByQualifierCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(qualifierCode, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Qualifier code already exists");
        }

        qualifier.setQualifierCode(qualifierCode);
        qualifier.setDescription(trimToNull(request.description()));

        return toResponse(qualifierRepository.save(qualifier));
    }

    @Transactional
    public void delete(UUID id) {
        Qualifier qualifier = getActiveQualifier(id);
        qualifier.softDelete();
        qualifierRepository.save(qualifier);
    }

    private Qualifier getActiveQualifier(UUID id) {
        return qualifierRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Qualifier not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private QualifierResponse toResponse(Qualifier qualifier) {
        return new QualifierResponse(
                qualifier.getId(),
                qualifier.getQualifierCode(),
                qualifier.getDescription()
        );
    }
}
