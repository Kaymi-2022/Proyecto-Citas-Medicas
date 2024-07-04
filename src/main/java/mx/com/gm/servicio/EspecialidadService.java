package mx.com.gm.servicio;

import mx.com.gm.domain.Especialidad;

import java.util.List;
import java.util.Optional;

public interface ConsultorioService {

    public void guardar(Especialidad especialidad);

    public void eliminar(Especialidad especialidad);

    public List<Especialidad> listarConsultorios();

    public Especialidad encontrarConsultorio(Especialidad especialidad);

    public Optional<Especialidad> obtenerConsultorioPorId(Long idConsultorio);
}
