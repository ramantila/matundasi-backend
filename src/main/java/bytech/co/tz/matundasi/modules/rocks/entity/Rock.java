package bytech.co.tz.matundasi.modules.rocks.entity;

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

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rocks")
public class Rock extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "rock_code", nullable = false)
    private String rockCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "regime", nullable = false)
    private String regime;
}
