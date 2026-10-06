package bytech.co.tz.matundasi.modules.mw_s.service;

import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import bytech.co.tz.matundasi.modules.mw_s.payload.MwsRequests;
import bytech.co.tz.matundasi.modules.mw_s.repository.MwsRepository;
import bytech.co.tz.matundasi.modules.mw_s.response.MwsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MwsService {
    private final MwsRepository mwsRepository;

    @Transactional
    public MwsResponse create(MwsRequests request) {
        Mws mws = Mws.builder()
                .hole(request.hole().trim())
                .type(request.type().trim())
                .hdepth(request.hdepth())
                .build();

        return toResponse(mwsRepository.save(mws));
    }

    @Transactional(readOnly = true)
    public List<MwsResponse> findAll() {
        return mwsRepository.findAllByDeletedAtIsNullOrderByHoleAscHdepthAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MwsResponse findById(UUID id) {
        return toResponse(getActiveMws(id));
    }

    @Transactional
    public MwsResponse update(UUID id, MwsRequests request) {
        Mws mws = getActiveMws(id);

        mws.setHole(request.hole().trim());
        mws.setType(request.type().trim());
        mws.setHdepth(request.hdepth());

        return toResponse(mwsRepository.save(mws));
    }

    @Transactional
    public void delete(UUID id) {
        Mws mws = getActiveMws(id);
        mws.softDelete();
        mwsRepository.save(mws);
    }

    private Mws getActiveMws(UUID id) {
        return mwsRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MW_S record not found"));
    }

    private MwsResponse toResponse(Mws mws) {
        return new MwsResponse(
                mws.getId(),
                mws.getHole(),
                mws.getType(),
                mws.getHdepth()
        );
    }
}
