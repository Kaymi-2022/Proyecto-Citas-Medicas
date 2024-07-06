package mx.com.gm.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(of = "idMedico")
@Table(name = "medicos")
public class Medicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;

    @Column(name = "nombre")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "foto_medico")
    private String foto;

    @ManyToOne
    @JoinColumn(name = "consultorio_id", nullable = false)
    @NotNull(message = "El consultorio no puede ser nulo")
    private Especialidad especialidad;

}
