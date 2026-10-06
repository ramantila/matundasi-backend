package bytech.co.tz.matundasi.modules.sizes.service;

import bytech.co.tz.matundasi.modules.sizes.entity.Size;
import bytech.co.tz.matundasi.modules.sizes.payload.SizeRequests;
import bytech.co.tz.matundasi.modules.sizes.repository.SizeRepository;
import bytech.co.tz.matundasi.modules.sizes.response.SizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final SizeRepository sizeRepository;

    @Transactional
    public SizeResponse create(SizeRequests request) {
        String code = request.code().trim();

        if (sizeRepository.existsByCodeIgnoreCaseAndDeletedAtIsNull(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Size code already exists");
        }

        Size size = Size.builder()
                .code(code)
                .grain(request.grain().trim())
                .build();

        return toResponse(sizeRepository.save(size));
    }

    @Transactional(readOnly = true)
    public List<SizeResponse> findAll() {
        return sizeRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SizeResponse findById(UUID id) {
        return toResponse(getActiveSize(id));
    }

    @Transactional
    public SizeResponse update(UUID id, SizeRequests request) {
        Size size = getActiveSize(id);
        String code = request.code().trim();

        if (sizeRepository.existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(code, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Size code already exists");
        }

        size.setCode(code);
        size.setGrain(request.grain().trim());

        return toResponse(sizeRepository.save(size));
    }

    @Transactional
    public void delete(UUID id) {
        Size size = getActiveSize(id);
        size.softDelete();
        sizeRepository.save(size);
    }

    private Size getActiveSize(UUID id) {
        return sizeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found"));
    }

    private SizeResponse toResponse(Size size) {
        return new SizeResponse(
                size.getId(),
                size.getCode(),
                size.getGrain()
        );
    }
}
