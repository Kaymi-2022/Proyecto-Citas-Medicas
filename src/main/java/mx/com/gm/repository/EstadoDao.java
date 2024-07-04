package mx.com.gm.repository;

import mx.com.gm.domain.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoDao  extends JpaRepository<EstadoCita, Long> {
}
