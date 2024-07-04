package mx.com.gm.servicio;

import mx.com.gm.domain.EstadoCita;
import mx.com.gm.repository.EstadoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoServiceImpl implements EstadoService{

    @Autowired
    private EstadoDao estadoDao;
    @Override
    public void guardarEstado(EstadoCita estado) {
        estadoDao.save(estado);
    }

    @Override
    public void eliminarEstado(EstadoCita estado) {
        estadoDao.delete(estado);
    }

    @Override
    public List<EstadoCita> listarEstados() {
        return estadoDao.findAll();
    }

    @Override
    public EstadoCita encontrarEstado(EstadoCita estado) {
        return estadoDao.findById(estado.getIdestadoCita()).orElse(null);
    }

    @Override
    public Optional<EstadoCita> obtenerEstadoPorId(Long id) {
        return estadoDao.findById(id);
    }
}
