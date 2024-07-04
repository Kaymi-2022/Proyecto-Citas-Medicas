package mx.com.gm.repository;

import mx.com.gm.domain.Horarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitasDao extends JpaRepository<Horarios, Long>{
    @Query("SELECT h FROM Citas h WHERE h.medicos.id = :idMedico")
    List<Horarios> findAllByMedicoId(@Param("idMedico") Long idMedico);
}
