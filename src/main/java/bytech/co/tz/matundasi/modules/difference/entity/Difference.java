package bytech.co.tz.matundasi.modules.difference.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.invoice.entity.Invoice;
import bytech.co.tz.matundasi.modules.projects.entity.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "differences")
public class Difference extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "sample_no_range")
    private String sampleNoRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "pre_lab")
    private String preLab;

    @Column(name = "lab")
    private String lab;

    @Column(name = "lab_loc")
    private String labLoc;

    @Column(name = "batch")
    private String batch;

    @Column(name = "dispatch_date")
    private LocalDate dispatchDate;

    @Column(name = "number_of_samples")
    private Integer numberOfSamples;

    @Column(name = "number_assayed")
    private Integer numberAssayed;

    @Column(name = "date_received")
    private LocalDate dateReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(name = "type")
    private String type;
}
