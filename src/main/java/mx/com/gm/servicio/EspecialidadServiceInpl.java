package mx.com.gm.servicio;

import mx.com.gm.domain.Especialidad;
import mx.com.gm.repository.ConsultorioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultorioServiceInpl implements ConsultorioService {
    @Autowired
    ConsultorioDao consultorioDao;

    @Override
    public void guardar(Especialidad especialidad) {
        consultorioDao.save(especialidad);
    }

    @Override
    public void eliminar(Especialidad especialidad) {
        consultorioDao.delete(especialidad);
    }

    @Override
    public List<Especialidad> listarConsultorios() {
        return consultorioDao.findAll();
    }

    @Override
    public Especialidad encontrarConsultorio(Especialidad especialidad) {
        return consultorioDao.findById(especialidad.getIdConsultorio()).orElse(null) ;
    }

    @Override
    public Optional<Especialidad> obtenerConsultorioPorId(Long idConsultorio) {
        return consultorioDao.findById(idConsultorio);
    }
}
