package bytech.co.tz.matundasi.modules.core_size.entity;

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
@Table(name = "core_sizes")
public class CoreSize extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mws_id", nullable = false)
    private Mws mws;

    @Column(name = "pre_collar_to", precision = 14, scale = 6)
    private BigDecimal preCollarTo;

    @Column(name = "pre_collar_method")
    private String preCollarMethod;

    @Column(name = "core_from", nullable = false, precision = 14, scale = 6)
    private BigDecimal coreFrom;

    @Column(name = "core_to", nullable = false, precision = 14, scale = 6)
    private BigDecimal coreTo;

    @Column(name = "core_size", nullable = false)
    private String coreSize;
}
