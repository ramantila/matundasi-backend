package bytech.co.tz.matundasi.modules.invoice.repository;

import bytech.co.tz.matundasi.modules.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findAllByDeletedAtIsNullOrderByOrderNumberAsc();

    Optional<Invoice> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByInvoiceNumberIgnoreCaseAndDeletedAtIsNull(String invoiceNumber);

    boolean existsByInvoiceNumberIgnoreCaseAndDeletedAtIsNullAndIdNot(String invoiceNumber, UUID id);
}
