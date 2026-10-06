package bytech.co.tz.matundasi.modules.lith.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import bytech.co.tz.matundasi.modules.weath.entity.Weathering;
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
@Table(name = "lithology")
public class Lithology extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "lithfrom", nullable = false, precision = 14, scale = 6)
    private BigDecimal lithfrom;

    @Column(name = "lithto", nullable = false, precision = 14, scale = 6)
    private BigDecimal lithto;

    @Column(name = "lithcode", nullable = false)
    private String lithcode;

    @Column(name = "quartz_percent", precision = 9, scale = 6)
    private BigDecimal quartzPercent;

    @Column(name = "pyrites_percent", precision = 9, scale = 6)
    private BigDecimal pyritesPercent;

    @Column(name = "alteration")
    private String alteration;

    @Column(name = "schistosity")
    private String schistosity;

    @Column(name = "grainsize")
    private String grainsize;

    @Column(name = "colour")
    private String colour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weathering_id")
    private Weathering weathcode;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
}
