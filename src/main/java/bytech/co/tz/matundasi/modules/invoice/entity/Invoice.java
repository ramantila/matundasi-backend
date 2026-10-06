package bytech.co.tz.matundasi.modules.invoice.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "lab_name")
    private String labName;

    @Column(name = "analysis")
    private String analysis;

    @Column(name = "job_number")
    private String jobNumber;

    @Column(name = "amount_in_us", precision = 18, scale = 6)
    private BigDecimal amountInUs;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "date_approved")
    private LocalDate dateApproved;
}
