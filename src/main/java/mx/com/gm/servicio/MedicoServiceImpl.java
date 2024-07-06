package mx.com.gm.servicio;

import mx.com.gm.domain.Medicos;
import mx.com.gm.repository.MedicoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoServiceImpl implements MedicoService{
    @Autowired
    MedicoDao medicoDao;

    @Override
    public void guardar(Medicos medico) {
        medicoDao.save(medico);
    }

    @Override
    public void eliminar(Medicos medico) {
        medicoDao.delete(medico);
    }

    @Override
    public List<Medicos> listarMedicos() {
        return medicoDao.findAll();
    }

    @Override
    public Medicos encontrarMedico(Medicos medico) {
        return medicoDao.findById(medico.getIdMedico()).orElse(null);
    }

    public List<Medicos> encontrarMedicoPorConsultorio(Long idConsultorio){
        return medicoDao.findMedicosByConsultorio(idConsultorio);
    }
}
