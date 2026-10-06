package bytech.co.tz.matundasi.modules.difference.service;

import bytech.co.tz.matundasi.modules.difference.entity.Difference;
import bytech.co.tz.matundasi.modules.difference.payload.DifferenceRequests;
import bytech.co.tz.matundasi.modules.difference.repository.DifferenceRepository;
import bytech.co.tz.matundasi.modules.difference.response.DifferenceResponse;
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
public class DifferenceService {
    private final DifferenceRepository differenceRepository;
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public DifferenceResponse create(DifferenceRequests request) {
        validate(request);

        Difference difference = new Difference();
        apply(difference, request);

        return toResponse(differenceRepository.save(difference));
    }

    @Transactional(readOnly = true)
    public List<DifferenceResponse> findAll() {
        return differenceRepository.findAllByDeletedAtIsNullOrderByOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DifferenceResponse findById(UUID id) {
        return toResponse(getActiveDifference(id));
    }

    @Transactional
    public DifferenceResponse update(UUID id, DifferenceRequests request) {
        Difference difference = getActiveDifference(id);
        validate(request);

        apply(difference, request);

        return toResponse(differenceRepository.save(difference));
    }

    @Transactional
    public void delete(UUID id) {
        Difference difference = getActiveDifference(id);
        difference.softDelete();
        differenceRepository.save(difference);
    }

    private void apply(Difference difference, DifferenceRequests request) {
        difference.setOrderNumber(request.orderNumber());
        difference.setSampleNoRange(trimToNull(request.sampleNoRange()));
        difference.setProject(request.projectId() == null ? null : getActiveProject(request.projectId()));
        difference.setPreLab(trimToNull(request.preLab()));
        difference.setLab(trimToNull(request.lab()));
        difference.setLabLoc(trimToNull(request.labLoc()));
        difference.setBatch(trimToNull(request.batch()));
        difference.setDispatchDate(request.dispatchDate());
        difference.setNumberOfSamples(request.numberOfSamples());
        difference.setNumberAssayed(request.numberAssayed());
        difference.setDateReceived(request.dateReceived());
        difference.setInvoice(request.invoiceId() == null ? null : getActiveInvoice(request.invoiceId()));
        difference.setType(trimToNull(request.type()));
    }

    private void validate(DifferenceRequests request) {
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

    private Difference getActiveDifference(UUID id) {
        return differenceRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Difference not found"));
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

    private DifferenceResponse toResponse(Difference difference) {
        return new DifferenceResponse(
                difference.getId(),
                difference.getOrderNumber(),
                difference.getSampleNoRange(),
                difference.getProject() == null ? null : difference.getProject().getId(),
                difference.getPreLab(),
                difference.getLab(),
                difference.getLabLoc(),
                difference.getBatch(),
                difference.getDispatchDate(),
                difference.getNumberOfSamples(),
                difference.getNumberAssayed(),
                difference.getDateReceived(),
                difference.getInvoice() == null ? null : difference.getInvoice().getId(),
                difference.getType()
        );
    }
}
