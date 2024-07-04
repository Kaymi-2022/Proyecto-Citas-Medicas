package mx.com.gm.servicio;

import mx.com.gm.domain.Horarios;

import java.util.List;
import java.util.Optional;

public interface CitasService {
    public List<Horarios> listarHorarios();
    public void guardarHorario(Horarios horario);
    public void eliminarHorario(Horarios horario);
    public Optional<Horarios> obtenerHorarioPorId(Long id);
}
