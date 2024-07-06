package mx.com.gm.domain;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Data
@Table(name = "estadoCita")
public class EstadoCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idestadoCita;

    @NotNull
    private String estado;
}
