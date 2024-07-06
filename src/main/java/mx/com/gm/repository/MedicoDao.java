package mx.com.gm.repository;

import mx.com.gm.domain.Medicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoDao extends JpaRepository<Medicos, Long>{
    @Query("SELECT m FROM Medicos m WHERE m.especialidad.id = :idConsultorio")
    List<Medicos> findMedicosByConsultorio(@Param("idConsultorio") Long idConsultorio);
}
