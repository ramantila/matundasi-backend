package bytech.co.tz.matundasi.modules.pits.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.lith.entity.Lithology;
import bytech.co.tz.matundasi.modules.sample.entity.Sample;
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
@Table(name = "pits")
public class Pit extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "pit_number", nullable = false)
    private String pitNumber;

    @Column(name = "local_e", precision = 15, scale = 6)
    private BigDecimal localE;

    @Column(name = "local_n", precision = 15, scale = 6)
    private BigDecimal localN;

    @Column(name = "utm_e", precision = 15, scale = 6)
    private BigDecimal utmE;

    @Column(name = "utm_n", precision = 15, scale = 6)
    private BigDecimal utmN;

    @Column(name = "rl_m", precision = 13, scale = 6)
    private BigDecimal rlM;

    @Column(name = "from_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal fromDepth;

    @Column(name = "to_depth", nullable = false, precision = 14, scale = 6)
    private BigDecimal toDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lithology_id")
    private Lithology lithology;

    @Column(name = "fraction")
    private String fraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    private Sample sample;

    @Column(name = "au_avg", precision = 14, scale = 6)
    private BigDecimal auAvg;

    @Column(name = "au_ppb", precision = 14, scale = 6)
    private BigDecimal auPpb;

    @Column(name = "au_rpt1", precision = 14, scale = 6)
    private BigDecimal auRpt1;

    @Column(name = "au_rpt2", precision = 14, scale = 6)
    private BigDecimal auRpt2;

    @Column(name = "laboratory")
    private String laboratory;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "study_type")
    private String studyType;
}
