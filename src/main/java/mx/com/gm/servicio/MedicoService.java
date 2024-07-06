package mx.com.gm.servicio;

import mx.com.gm.domain.Medicos;

import java.util.List;

public interface MedicoService {
    public void guardar(Medicos medico);

    public void eliminar(Medicos medico);

    public List<Medicos> listarMedicos();

    public Medicos encontrarMedico(Medicos medico);
}
