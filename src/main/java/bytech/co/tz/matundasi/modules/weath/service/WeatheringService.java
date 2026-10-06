package bytech.co.tz.matundasi.modules.weath.service;

import bytech.co.tz.matundasi.modules.weath.entity.Weathering;
import bytech.co.tz.matundasi.modules.weath.payload.WeatheringRequests;
import bytech.co.tz.matundasi.modules.weath.repository.WeatheringRepository;
import bytech.co.tz.matundasi.modules.weath.response.WeatheringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeatheringService {
    private final WeatheringRepository weatheringRepository;

    @Transactional
    public WeatheringResponse create(WeatheringRequests request) {
        String code = request.code().trim();

        if (weatheringRepository.existsByCodeIgnoreCaseAndDeletedAtIsNull(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weathering code already exists");
        }

        Weathering weathering = Weathering.builder()
                .code(code)
                .description(trimToNull(request.description()))
                .build();

        return toResponse(weatheringRepository.save(weathering));
    }

    @Transactional(readOnly = true)
    public List<WeatheringResponse> findAll() {
        return weatheringRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WeatheringResponse findById(UUID id) {
        return toResponse(getActiveWeathering(id));
    }

    @Transactional
    public WeatheringResponse update(UUID id, WeatheringRequests request) {
        Weathering weathering = getActiveWeathering(id);
        String code = request.code().trim();

        if (weatheringRepository.existsByCodeIgnoreCaseAndDeletedAtIsNullAndIdNot(code, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weathering code already exists");
        }

        weathering.setCode(code);
        weathering.setDescription(trimToNull(request.description()));

        return toResponse(weatheringRepository.save(weathering));
    }

    @Transactional
    public void delete(UUID id) {
        Weathering weathering = getActiveWeathering(id);
        weathering.softDelete();
        weatheringRepository.save(weathering);
    }

    private Weathering getActiveWeathering(UUID id) {
        return weatheringRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Weathering not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private WeatheringResponse toResponse(Weathering weathering) {
        return new WeatheringResponse(
                weathering.getId(),
                weathering.getCode(),
                weathering.getDescription()
        );
    }
}
