package mx.com.gm.repository;

import mx.com.gm.domain.EstadoCita;
import mx.com.gm.domain.Horarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface HorariosDao extends JpaRepository<Horarios, Long> {
    @Query("SELECT h FROM Horarios h WHERE h.medicos.id = :idMedico")
    List<Horarios> findAllByMedicoId(@Param("idMedico") Long idMedico);

    @Query("SELECT h FROM Horarios h")
    List<Horarios> ListarTodo();

    @Modifying
    @Transactional
    @Query(value = "UPDATE hospital.horarios SET idestado_cita = :estadoDisponible WHERE id = :idHorario", nativeQuery = true)
    int actualizarEstadoHorario(@Param("idHorario") Long idHorario,
            @Param("estadoDisponible") Long estadoDisponible);
}
