package mx.com.gm.servicio;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import mx.com.gm.domain.HistorialCitados;
import mx.com.gm.repository.HistorialDao;

@Service
public class HistorialCitasServiceImpl implements HistorialCitasService {

    @Autowired
    private HistorialDao historialDao;

    @Override
    public void guardar(HistorialCitados historialPaciente) {
        historialDao.save(historialPaciente);
    }

    @Override
    public void eliminar(HistorialCitados historialPaciente) {
        historialDao.delete(historialPaciente);
    }

    @Override
    public List<HistorialCitados> listarHistorialPaciente() {
        return historialDao.findAll();
    }

    @Override
    public HistorialCitados encontrarHistorialPaciente(HistorialCitados historialPaciente) {
        return historialDao.findById(historialPaciente.getId()).orElse(null);
    }

    public List<EspecialidadTotalDTO> getTotalCitadosByEspecialidad() {
        return historialDao.findTotalCitadosByEspecialidad().stream()
                .map(result -> new EspecialidadTotalDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<SituacionCountDTO> getSituacionCount() {
        return historialDao.findSituacionByConsultorio().stream()
                .map(result -> new SituacionCountDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<MesCountDTO> getCountByMonth() {
        return historialDao.findCountByMonth().stream()
                .map(result -> new MesCountDTO((Integer) result[0], (Integer) result[1], ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<HistorialCitados> getHistorialCitas(String dni) {
        return historialDao.findByDNI(dni);
    }

    @Data
    @AllArgsConstructor
    public static class EspecialidadTotalDTO {
        private String especialidad;
        private Long total;
    }

    @Data
    @AllArgsConstructor
    public static class SituacionCountDTO {
        private String situacion;
        private Long total;
    }

    @Data
    public static class MesCountDTO {
        private String mes;
        private Long total;

        public MesCountDTO(Integer month, Integer year, Long total) {
            this.mes = YearMonth.of(year, month).toString();
            this.total = total;
        }
    }
}
