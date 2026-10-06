package bytech.co.tz.matundasi.modules.per_area_hole.entity;

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
@Table(name = "per_area_holes")
public class PerAreaHole extends CreatedAtUpdatedAtExtender {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "buh_diamond")
    private String buhDiamond;

    @Column(name = "buh_rab")
    private String buhRab;

    @Column(name = "buh_rc")
    private String buhRc;

    @Column(name = "kilamo_diamond")
    private String kilamoDiamond;

    @Column(name = "kilamo_rab")
    private String kilamoRab;

    @Column(name = "kilamo_rc")
    private String kilamoRc;

    @Column(name = "nyase_rab")
    private String nyaseRab;

    @Column(name = "nyase_rc")
    private String nyaseRc;

    @Column(name = "nyase_ddh")
    private String nyaseDdh;
}
