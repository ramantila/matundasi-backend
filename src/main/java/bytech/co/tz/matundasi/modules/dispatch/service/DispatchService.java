package bytech.co.tz.matundasi.modules.dispatch.service;

import bytech.co.tz.matundasi.modules.dispatch.entity.Dispatch;
import bytech.co.tz.matundasi.modules.dispatch.payload.DispatchRequests;
import bytech.co.tz.matundasi.modules.dispatch.repository.DispatchRepository;
import bytech.co.tz.matundasi.modules.dispatch.response.DispatchResponse;
import bytech.co.tz.matundasi.modules.invoice.entity.Invoice;
import bytech.co.tz.matundasi.modules.invoice.repository.InvoiceRepository;
import bytech.co.tz.matundasi.modules.projects.entity.Project;
import bytech.co.tz.matundasi.modules.projects.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DispatchService {
    private final DispatchRepository dispatchRepository;
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public DispatchResponse create(DispatchRequests request) {
        validate(request);

        Dispatch dispatch = new Dispatch();
        apply(dispatch, request);

        return toResponse(dispatchRepository.save(dispatch));
    }

    @Transactional(readOnly = true)
    public List<DispatchResponse> findAll() {
        return dispatchRepository.findAllByDeletedAtIsNullOrderByOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DispatchResponse findById(UUID id) {
        return toResponse(getActiveDispatch(id));
    }

    @Transactional
    public DispatchResponse update(UUID id, DispatchRequests request) {
        Dispatch dispatch = getActiveDispatch(id);
        validate(request);

        apply(dispatch, request);

        return toResponse(dispatchRepository.save(dispatch));
    }

    @Transactional
    public void delete(UUID id) {
        Dispatch dispatch = getActiveDispatch(id);
        dispatch.softDelete();
        dispatchRepository.save(dispatch);
    }

    private void apply(Dispatch dispatch, DispatchRequests request) {
        dispatch.setOrderNumber(request.orderNumber());
        dispatch.setSampleNoRange(trimToNull(request.sampleNoRange()));
        dispatch.setOriginator(trimToNull(request.originator()));
        dispatch.setGridHoles(trimToNull(request.gridHoles()));
        dispatch.setProject(request.projectId() == null ? null : getActiveProject(request.projectId()));
        dispatch.setPreLab(trimToNull(request.preLab()));
        dispatch.setLab(trimToNull(request.lab()));
        dispatch.setLabLoc(trimToNull(request.labLoc()));
        dispatch.setBatch(trimToNull(request.batch()));
        dispatch.setDispatchDate(request.dispatchDate());
        dispatch.setNumberOfSamples(request.numberOfSamples());
        dispatch.setNumberAssayed(request.numberAssayed());
        dispatch.setDateReceived(request.dateReceived());
        dispatch.setInvoice(request.invoiceId() == null ? null : getActiveInvoice(request.invoiceId()));
        dispatch.setType(trimToNull(request.type()));
    }

    private void validate(DispatchRequests request) {
        if (request.numberOfSamples() != null && request.numberAssayed() != null
                && request.numberAssayed() > request.numberOfSamples()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "number_assayed cannot be greater than number_of_samples");
        }
        if (request.dispatchDate() != null && request.dateReceived() != null
                && request.dateReceived().isBefore(request.dispatchDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "date_received cannot be before dispatch_date");
        }
    }

    private Dispatch getActiveDispatch(UUID id) {
        return dispatchRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispatch not found"));
    }

    private Project getActiveProject(UUID projectId) {
        return projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private Invoice getActiveInvoice(UUID invoiceId) {
        return invoiceRepository.findByIdAndDeletedAtIsNull(invoiceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private DispatchResponse toResponse(Dispatch dispatch) {
        return new DispatchResponse(
                dispatch.getId(),
                dispatch.getOrderNumber(),
                dispatch.getSampleNoRange(),
                dispatch.getOriginator(),
                dispatch.getGridHoles(),
                dispatch.getProject() == null ? null : dispatch.getProject().getId(),
                dispatch.getPreLab(),
                dispatch.getLab(),
                dispatch.getLabLoc(),
                dispatch.getBatch(),
                dispatch.getDispatchDate(),
                dispatch.getNumberOfSamples(),
                dispatch.getNumberAssayed(),
                dispatch.getDateReceived(),
                dispatch.getInvoice() == null ? null : dispatch.getInvoice().getId(),
                dispatch.getType()
        );
    }
}
