package mx.com.gm.servicio;

import mx.com.gm.domain.Horarios;
import mx.com.gm.repository.HorariosDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorariosServiceImpl implements HorariosService {
    @Autowired
    private HorariosDao horariosDao;

    @Override
    public List<Horarios> listarHorarios() {
        return horariosDao.findAll();
    }

    @Override
    public void guardarHorario(Horarios horario) {
        horariosDao.save(horario);
    }

    @Override
    public void eliminarHorario(Horarios horario) {
        horariosDao.delete(horario);
    }

    @Override
    public Optional<Horarios> obtenerHorarioPorId(Long id) {
        return horariosDao.findById(id);
    }

    public List<Horarios> obtenerHorarioPorIdDoctor(Long idMedico) {
        return horariosDao.findAllByMedicoId(idMedico);
    }

    public Horarios obtenerHorario(Long id) {
        return horariosDao.findById(id).orElse(null);
    }

    public int actualizarEstadoHorario(Long idHorario, Long estadoDisponible) {
        return horariosDao.actualizarEstadoHorario(idHorario, estadoDisponible);
    }
}
