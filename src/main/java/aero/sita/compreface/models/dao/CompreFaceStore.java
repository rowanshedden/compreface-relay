package aero.sita.compreface.models.dao;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@Table(name = "compreface", schema = "public")
public class CompreFaceStore {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "apikey", nullable = false)
    private String apikey;

}
