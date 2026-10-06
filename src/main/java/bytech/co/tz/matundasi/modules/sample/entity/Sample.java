package bytech.co.tz.matundasi.modules.sample.entity;

import bytech.co.tz.matundasi.common.entity.CreatedAtUpdatedAtExtender;
import bytech.co.tz.matundasi.modules.elements.entity.Element;
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
@Table(name = "samples")
public class Sample extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "sample", nullable = false)
    private String sample;

    @Column(name = "flag")
    private String flag;

    @Column(name = "flag_id")
    private String flagId;

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

    @Column(name = "tenement")
    private String tenement;

    @Column(name = "map_125")
    private String map125;

    @Column(name = "map_50")
    private String map50;

    @Column(name = "code")
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "au", precision = 14, scale = 6)
    private BigDecimal au;

    @Column(name = "au_rp1", precision = 14, scale = 6)
    private BigDecimal auRp1;

    @Column(name = "au_rp2", precision = 14, scale = 6)
    private BigDecimal auRp2;

    @Column(name = "au2", precision = 14, scale = 6)
    private BigDecimal au2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    private Element element;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "claim")
    private String claim;
}
