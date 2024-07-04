package mx.com.gm.repository;

import mx.com.gm.domain.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadDao extends JpaRepository<Especialidad, Long> {

}
