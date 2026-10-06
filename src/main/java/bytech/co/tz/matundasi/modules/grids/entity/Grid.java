package bytech.co.tz.matundasi.modules.grids.entity;

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
@Table(name = "grids")
public class Grid extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "local_name", nullable = false)
    private String localName;

    @Column(name = "e1o", precision = 16, scale = 6)
    private BigDecimal e1o;

    @Column(name = "n1o", precision = 16, scale = 6)
    private BigDecimal n1o;

    @Column(name = "e2o", precision = 16, scale = 6)
    private BigDecimal e2o;

    @Column(name = "n2o", precision = 16, scale = 6)
    private BigDecimal n2o;

    @Column(name = "delta", precision = 14, scale = 6)
    private BigDecimal delta;

    @Column(name = "e1o2", precision = 16, scale = 6)
    private BigDecimal e1o2;

    @Column(name = "n1o2", precision = 16, scale = 6)
    private BigDecimal n1o2;

    @Column(name = "e2o2", precision = 16, scale = 6)
    private BigDecimal e2o2;

    @Column(name = "n2o2", precision = 16, scale = 6)
    private BigDecimal n2o2;

    @Column(name = "zone")
    private String zone;

    @Column(name = "datum")
    private String datum;

    @Column(name = "original_grid")
    private String originalGrid;

    @Column(name = "derived_grid")
    private String derivedGrid;
}
