package bytech.co.tz.matundasi.modules.prospect.entity;

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
@Table(name = "prospects")
public class Prospect extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "min_x", precision = 15, scale = 6)
    private BigDecimal minX;

    @Column(name = "min_y", precision = 15, scale = 6)
    private BigDecimal minY;

    @Column(name = "max_x", precision = 15, scale = 6)
    private BigDecimal maxX;

    @Column(name = "max_y", precision = 15, scale = 6)
    private BigDecimal maxY;
}
