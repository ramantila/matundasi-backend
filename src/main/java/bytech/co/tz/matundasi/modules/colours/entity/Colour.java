package bytech.co.tz.matundasi.modules.colours.entity;

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
@Table(name = "colours")
public class Colour extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "colour_code", nullable = false)
    private String colourCode;

    @Column(name = "colour_name", nullable = false)
    private String colourName;
}
