package mx.com.gm.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "historialcitados")
public class HistorialCitados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotEmpty
    private String paciente;

    @NotEmpty
    private String consultorio;

    @NotEmpty
    private String medico;

    @NotEmpty
    private String actividad;

    @NotNull
    private LocalTime hora;

    @NotEmpty
    private String situacion;

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
