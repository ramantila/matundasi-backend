package bytech.co.tz.matundasi.modules.drill.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.drill.enums.MoistureState;
import bytech.co.tz.matundasi.modules.drill.enums.SampleMethod;
import bytech.co.tz.matundasi.modules.elements.entity.Element;
import bytech.co.tz.matundasi.modules.mw_s.entity.Mws;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "drills")
public class Drill extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "sample", nullable = false)
    private String sample;

    @Column(name = "from_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal fromDepth;

    @Column(name = "to_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal toDepth;

    @Column(name = "flag")
    private String flag;

    @Column(name = "flag_id")
    private String flagId;

    @Column(name = "exclude", nullable = false)
    private Boolean exclude;

    @Enumerated(EnumType.STRING)
    @Column(name = "riffle_grab", length = 16)
    private SampleMethod riffleGrab;

    @Column(name = "recovery_percent", precision = 9, scale = 6)
    private BigDecimal recoveryPercent;

    @Enumerated(EnumType.STRING)
    @Column(name = "wet_dry", length = 16)
    private MoistureState wetDry;

    @Column(name = "au", precision = 14, scale = 6)
    private BigDecimal au;

    @Column(name = "au_rp1", precision = 14, scale = 6)
    private BigDecimal auRp1;

    @Column(name = "au_rp2", precision = 14, scale = 6)
    private BigDecimal auRp2;

    @Column(name = "au_rp3", precision = 14, scale = 6)
    private BigDecimal auRp3;

    @Column(name = "au_rp4", precision = 14, scale = 6)
    private BigDecimal auRp4;

    @Column(name = "au2", precision = 14, scale = 6)
    private BigDecimal au2;

    @Column(name = "au2_rp1", precision = 14, scale = 6)
    private BigDecimal au2Rp1;

    @Column(name = "au2_plot", precision = 14, scale = 6)
    private BigDecimal au2Plot;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "element_id", nullable = false)
    private Element element;
}
