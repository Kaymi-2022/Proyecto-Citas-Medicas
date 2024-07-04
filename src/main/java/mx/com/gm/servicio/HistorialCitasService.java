package mx.com.gm.servicio;


import java.util.List;
import mx.com.gm.domain.HistorialCitados;

public interface HistorialCitasService {

    public void guardar(HistorialCitados historialPaciente);

    public void eliminar(HistorialCitados historialPaciente);

    public List<HistorialCitados> listarHistorialPaciente();

    public HistorialCitados encontrarHistorialPaciente(HistorialCitados historialPaciente);
}
