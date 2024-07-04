package mx.com.gm.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

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
    private Set<Medicos> medicos= new HashSet<>();;


}
