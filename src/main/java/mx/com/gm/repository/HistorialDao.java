package mx.com.gm.repository;

import mx.com.gm.domain.HistorialCitados;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistorialDao extends JpaRepository<HistorialCitados, Long> {

    @Query("SELECT h.consultorio, COUNT(h) FROM HistorialCitados h GROUP BY h.consultorio")
    List<Object[]> findTotalCitadosByEspecialidad();

    @Query("SELECT h.situacion, COUNT(h) FROM HistorialCitados h GROUP BY h.situacion")
    List<Object[]> findSituacionByConsultorio();

    @Query("SELECT FUNCTION('MONTH', h.fecha), FUNCTION('YEAR', h.fecha), COUNT(h) FROM HistorialCitados h GROUP BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha) ORDER BY FUNCTION('YEAR', h.fecha), FUNCTION('MONTH', h.fecha)")
    List<Object[]> findCountByMonth();
}
