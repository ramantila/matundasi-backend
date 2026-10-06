package bytech.co.tz.matundasi.modules.invoice.service;

import bytech.co.tz.matundasi.modules.invoice.entity.Invoice;
import bytech.co.tz.matundasi.modules.invoice.payload.InvoiceRequests;
import bytech.co.tz.matundasi.modules.invoice.repository.InvoiceRepository;
import bytech.co.tz.matundasi.modules.invoice.response.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public InvoiceResponse create(InvoiceRequests request) {
        String invoiceNumber = request.invoiceNumber().trim();

        if (invoiceRepository.existsByInvoiceNumberIgnoreCaseAndDeletedAtIsNull(invoiceNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invoice number already exists");
        }

        Invoice invoice = new Invoice();
        apply(invoice, invoiceNumber, request);

        return toResponse(invoiceRepository.save(invoice));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findAll() {
        return invoiceRepository.findAllByDeletedAtIsNullOrderByOrderNumberAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse findById(UUID id) {
        return toResponse(getActiveInvoice(id));
    }

    @Transactional
    public InvoiceResponse update(UUID id, InvoiceRequests request) {
        Invoice invoice = getActiveInvoice(id);
        String invoiceNumber = request.invoiceNumber().trim();

        if (invoiceRepository.existsByInvoiceNumberIgnoreCaseAndDeletedAtIsNullAndIdNot(invoiceNumber, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invoice number already exists");
        }

        apply(invoice, invoiceNumber, request);

        return toResponse(invoiceRepository.save(invoice));
    }

    @Transactional
    public void delete(UUID id) {
        Invoice invoice = getActiveInvoice(id);
        invoice.softDelete();
        invoiceRepository.save(invoice);
    }

    private void apply(Invoice invoice, String invoiceNumber, InvoiceRequests request) {
        invoice.setOrderNumber(request.orderNumber());
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setLabName(trimToNull(request.labName()));
        invoice.setAnalysis(trimToNull(request.analysis()));
        invoice.setJobNumber(trimToNull(request.jobNumber()));
        invoice.setAmountInUs(request.amountInUs());
        invoice.setAccountCode(trimToNull(request.accountCode()));
        invoice.setInvoiceDate(request.invoiceDate());
        invoice.setDateApproved(request.dateApproved());
    }

    private Invoice getActiveInvoice(UUID id) {
        return invoiceRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getOrderNumber(),
                invoice.getInvoiceNumber(),
                invoice.getLabName(),
                invoice.getAnalysis(),
                invoice.getJobNumber(),
                invoice.getAmountInUs(),
                invoice.getAccountCode(),
                invoice.getInvoiceDate(),
                invoice.getDateApproved()
        );
    }
}
