package mx.com.gm.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.persistence.CascadeType;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(of = "idConsultorio")
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultorio;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "foto")
    private String foto;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medicos> medicos;
}
