package bytech.co.tz.matundasi.modules.drill_survey.entity;

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
@Table(name = "drill_surveys")
public class DrillSurvey extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "downdepth", nullable = false, precision = 14, scale = 6)
    private BigDecimal downdepth;

    @Column(name = "dip", nullable = false, precision = 9, scale = 6)
    private BigDecimal dip;

    @Column(name = "azimuth", nullable = false, precision = 9, scale = 6)
    private BigDecimal azimuth;

    @Column(name = "utm_az", nullable = false, precision = 9, scale = 6)
    private BigDecimal utmAz;
}
