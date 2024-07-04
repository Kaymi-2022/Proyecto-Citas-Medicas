package mx.com.gm.servicio;

import mx.com.gm.domain.Horarios;
import mx.com.gm.repository.CitasDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitasServiceImpl implements CitasService {
    @Autowired
    private CitasDao citasDao;

    @Override
    public List<Horarios> listarHorarios() {
        return citasDao.findAll();
    }

    @Override
    public void guardarHorario(Horarios horario) {
        citasDao.save(horario);
    }

    @Override
    public void eliminarHorario(Horarios horario) {
        citasDao.delete(horario);
    }

    @Override
    public Optional<Horarios> obtenerHorarioPorId(Long id) {
        return citasDao.findById(id);
    }

    public List<Horarios> obtenerHorarioPorIdDoctor(Long idMedico) {
        return citasDao.findAllByMedicoId(idMedico);
    }

}
