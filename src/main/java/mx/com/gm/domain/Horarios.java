package mx.com.gm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "citas")
public class Citas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "idestadoCita")
    private EstadoCita idestadoCita;

    @ManyToOne
    @JoinColumn(name = "medico_id",nullable = false)
    @NotNull(message = "El medico no puede ser nulo")
    private Medicos medicos;

}
