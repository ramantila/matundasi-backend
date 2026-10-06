package bytech.co.tz.matundasi.modules.invoice.controller;

import bytech.co.tz.matundasi.common.response.HttpResponse;
import bytech.co.tz.matundasi.modules.invoice.payload.InvoiceRequests;
import bytech.co.tz.matundasi.modules.invoice.response.InvoiceResponse;
import bytech.co.tz.matundasi.modules.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Lab invoice management API")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create a new invoice",
            description = "Registers a new lab invoice. Fails with 409 if the invoice number is already used by " +
                    "another active invoice.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Invoice number already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<InvoiceResponse>> create(@Valid @RequestBody InvoiceRequests request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new HttpResponse<>(true, "Invoice created successfully.", HttpStatus.CREATED, invoiceService.create(request)));
    }

    @GetMapping
    @Operation(summary = "List invoices", description = "Returns all active (non-deleted) invoices sorted by order number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoices returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<List<InvoiceResponse>>> findAll() {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Invoices retrieved successfully.", HttpStatus.OK, invoiceService.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an invoice by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice returned"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<InvoiceResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Invoice retrieved successfully.", HttpStatus.OK, invoiceService.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an invoice",
            description = "Replaces all fields of an existing invoice. Fails with 409 if the new invoice number " +
                    "is already used by another active invoice.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "409", description = "Invoice number already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<InvoiceResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody InvoiceRequests request) {
        return ResponseEntity.ok(
                new HttpResponse<>(true, "Invoice updated successfully.", HttpStatus.OK, invoiceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice", description = "Soft-deletes the invoice (sets deleted_at).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice deleted"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<HttpResponse<Void>> delete(@PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(new HttpResponse<>(true, "Invoice deleted successfully.", HttpStatus.OK));
    }
}
