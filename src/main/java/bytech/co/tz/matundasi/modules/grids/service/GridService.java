package bytech.co.tz.matundasi.modules.grids.service;

import bytech.co.tz.matundasi.modules.grids.entity.Grid;
import bytech.co.tz.matundasi.modules.grids.payload.GridRequests;
import bytech.co.tz.matundasi.modules.grids.repository.GridRepository;
import bytech.co.tz.matundasi.modules.grids.response.GridResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GridService {
    private final GridRepository gridRepository;

    @Transactional
    public GridResponse create(GridRequests request) {
        String localName = request.localName().trim();

        if (gridRepository.existsByLocalNameIgnoreCaseAndDeletedAtIsNull(localName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Grid local name already exists");
        }

        Grid grid = new Grid();
        apply(grid, localName, request);

        return toResponse(gridRepository.save(grid));
    }

    @Transactional(readOnly = true)
    public List<GridResponse> findAll() {
        return gridRepository.findAllByDeletedAtIsNullOrderByLocalNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GridResponse findById(UUID id) {
        return toResponse(getActiveGrid(id));
    }

    @Transactional
    public GridResponse update(UUID id, GridRequests request) {
        Grid grid = getActiveGrid(id);
        String localName = request.localName().trim();

        if (gridRepository.existsByLocalNameIgnoreCaseAndDeletedAtIsNullAndIdNot(localName, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Grid local name already exists");
        }

        apply(grid, localName, request);

        return toResponse(gridRepository.save(grid));
    }

    @Transactional
    public void delete(UUID id) {
        Grid grid = getActiveGrid(id);
        grid.softDelete();
        gridRepository.save(grid);
    }

    private void apply(Grid grid, String localName, GridRequests request) {
        grid.setLocalName(localName);
        grid.setE1o(request.e1o());
        grid.setN1o(request.n1o());
        grid.setE2o(request.e2o());
        grid.setN2o(request.n2o());
        grid.setDelta(request.delta());
        grid.setE1o2(request.e1o2());
        grid.setN1o2(request.n1o2());
        grid.setE2o2(request.e2o2());
        grid.setN2o2(request.n2o2());
        grid.setZone(trimToNull(request.zone()));
        grid.setDatum(trimToNull(request.datum()));
        grid.setOriginalGrid(trimToNull(request.originalGrid()));
        grid.setDerivedGrid(trimToNull(request.derivedGrid()));
    }

    private Grid getActiveGrid(UUID id) {
        return gridRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grid not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public GridResponse toResponse(Grid grid) {
        return new GridResponse(
                grid.getId(),
                grid.getLocalName(),
                grid.getE1o(),
                grid.getN1o(),
                grid.getE2o(),
                grid.getN2o(),
                grid.getDelta(),
                grid.getE1o2(),
                grid.getN1o2(),
                grid.getE2o2(),
                grid.getN2o2(),
                grid.getZone(),
                grid.getDatum(),
                grid.getOriginalGrid(),
                grid.getDerivedGrid()
        );
    }
}
