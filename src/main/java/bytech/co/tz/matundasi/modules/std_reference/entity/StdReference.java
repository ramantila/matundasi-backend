package bytech.co.tz.matundasi.modules.std_reference.entity;

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
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "std_references")
public class StdReference extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "standard", nullable = false)
    private String standard;

    @Column(name = "ref_au", precision = 14, scale = 6)
    private BigDecimal refAu;

    @Column(name = "au_units")
    private String auUnits;

    @Column(name = "n")
    private Integer n;

    @Column(name = "ref_au_ar_aas", precision = 14, scale = 6)
    private BigDecimal refAuArAas;

    @Column(name = "au_ar_units")
    private String auArUnits;

    @Column(name = "ref_au_fa_50", precision = 14, scale = 6)
    private BigDecimal refAuFa50;

    @Column(name = "elements_unit")
    private String elementsUnit;
}
