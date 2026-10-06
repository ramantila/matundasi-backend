package bytech.co.tz.matundasi.modules.rab_collar.entity;

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
@Table(name = "rab_collars")
public class RabCollar extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "type")
    private String type;

    @Column(name = "utm_e", precision = 15, scale = 6)
    private BigDecimal utmE;

    @Column(name = "utm_n", precision = 15, scale = 6)
    private BigDecimal utmN;

    @Column(name = "hdepth", precision = 14, scale = 6)
    private BigDecimal hdepth;
}
