package bytech.co.tz.matundasi.modules.schs.service;

import bytech.co.tz.matundasi.modules.schs.entity.Schistosity;
import bytech.co.tz.matundasi.modules.schs.payload.SchistosityRequests;
import bytech.co.tz.matundasi.modules.schs.repository.SchistosityRepository;
import bytech.co.tz.matundasi.modules.schs.response.SchistosityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchistosityService {
    private final SchistosityRepository schistosityRepository;

    @Transactional
    public SchistosityResponse create(SchistosityRequests request) {
        String code = request.code().trim();

        if (schistosityRepository.existsByCodeIgnoreCaseAndDeletedAtIsNull(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schistosity code already exists");
        }

        Schistosity schistosity = Schistosity.builder()
                .code(code)
                .schistosity(request.schistosity().trim())
                .build();

        return toResponse(schistosityRepository.save(schistosity));
    }

    @Transactional(readOnly = true)
    public List<SchistosityResponse> findAll() {
        return schistosityRepository.findAllByDeletedAtIsNullOrderByCodeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SchistosityResponse findById(UUID id) {
        return toResponse(getActiveSchistosity(id));
    }

    @Transactional
    public SchistosityResponse update(UUID id, SchistosityRequests request) {
        Schistosity schistosity = getActiveSchistosity(id);
        String code = request.code().trim();

        if (schistosityRepository.existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(code, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schistosity code already exists");
        }

        schistosity.setCode(code);
        schistosity.setSchistosity(request.schistosity().trim());

        return toResponse(schistosityRepository.save(schistosity));
    }

    @Transactional
    public void delete(UUID id) {
        Schistosity schistosity = getActiveSchistosity(id);
        schistosity.softDelete();
        schistosityRepository.save(schistosity);
    }

    private Schistosity getActiveSchistosity(UUID id) {
        return schistosityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schistosity not found"));
    }

    private SchistosityResponse toResponse(Schistosity schistosity) {
        return new SchistosityResponse(
                schistosity.getId(),
                schistosity.getCode(),
                schistosity.getSchistosity()
        );
    }
}
