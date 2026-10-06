package bytech.co.tz.matundasi.modules.composite.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
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

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "composites")
public class Composite extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "from_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal fromDepth;

    @Column(name = "to_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal toDepth;

    @Column(name = "length", nullable = false, precision = 14, scale = 6)
    private BigDecimal length;

    @Column(name = "average_ni", precision = 14, scale = 6)
    private BigDecimal averageNi;

    @Column(name = "average_co", precision = 14, scale = 6)
    private BigDecimal averageCo;
}
