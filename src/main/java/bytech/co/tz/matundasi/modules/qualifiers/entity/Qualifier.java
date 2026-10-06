package bytech.co.tz.matundasi.modules.qualifiers.entity;

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
@Table(name = "qualifiers")
public class Qualifier extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "qualifier_code", nullable = false)
    private String qualifierCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
