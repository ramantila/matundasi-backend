package bytech.co.tz.matundasi.modules.collar.entity;

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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "collars")
public class Collar extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "utm_e", precision = 15, scale = 6)
    private BigDecimal utmE;

    @Column(name = "utm_n", precision = 15, scale = 6)
    private BigDecimal utmN;

    @Column(name = "zone")
    private String zone;

    @Column(name = "local_x", precision = 15, scale = 6)
    private BigDecimal localX;

    @Column(name = "local_y", precision = 15, scale = 6)
    private BigDecimal localY;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "map_125")
    private String map125;

    @Column(name = "map_50")
    private String map50;

    @Column(name = "tenement")
    private String tenement;

    @Column(name = "claim")
    private String claim;

    @Column(name = "local_map_sheet")
    private String localMapSheet;

    @Column(name = "rl", precision = 13, scale = 6)
    private BigDecimal rl;

    @Column(name = "azimuth", precision = 9, scale = 6)
    private BigDecimal azimuth;

    @Column(name = "utm_az", precision = 9, scale = 6)
    private BigDecimal utmAz;

    @Column(name = "hdepth", precision = 14, scale = 6)
    private BigDecimal hdepth;

    @Column(name = "date_drilled")
    private LocalDate dateDrilled;
}
