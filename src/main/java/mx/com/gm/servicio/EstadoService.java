package mx.com.gm.servicio;

import mx.com.gm.domain.EstadoCita;

import java.util.List;
import java.util.Optional;

public interface EstadoService {
    public void guardarEstado(EstadoCita estado);
    public void eliminarEstado(EstadoCita estado);
    public List<EstadoCita> listarEstados();
    public EstadoCita encontrarEstado(EstadoCita estado);
    public Optional<EstadoCita> obtenerEstadoPorId(Long id);
}
