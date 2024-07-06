package mx.com.gm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.gm.domain.*;
import mx.com.gm.servicio.EspecialidadServiceInpl;
import mx.com.gm.servicio.HorariosServiceImpl;
import mx.com.gm.servicio.MedicoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@Slf4j
public class ControllerProcesoCita {
    @Autowired
    EspecialidadServiceInpl especialidadServiceInpl;
    @Autowired
    MedicoServiceImpl medicoServiceImpl;

    @Autowired
    HorariosServiceImpl horarioServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("/paginaInicioProcesoCita")
    public String pagina1(Model model) {
        return "paginaInicioProcesoCita";
    }

    @GetMapping("/plantillaConsultorios")
    public String seleccionarEspecialidad(@RequestParam String valor, Model model) {

        var listaconsultorios = especialidadServiceInpl.listarConsultorios();
        model.addAttribute("consultorios", listaconsultorios);
        model.addAttribute("valor", valor);
        return "plantillaConsultorios";
    }

    @GetMapping("/plantillaMedicos")
    public String seleccionarConsultorio(@Valid Especialidad especialidad, Model model) {
        model.addAttribute("medicos", medicoServiceImpl.encontrarMedicoPorConsultorio(especialidad.getIdConsultorio()));
        model.addAttribute("nombreConsultorio", especialidad.getNombre());
        return "plantillaMedicos";
    }


    @GetMapping("/obtenerHorariosCalendario")
    public String getCalendar(@RequestParam String consultorio, Medicos medico, Model model) throws JsonProcessingException {
        List<Horarios> horarios = horarioServiceImpl.obtenerHorarioPorIdDoctor(medico.getIdMedico());

        // Si la lista de horarios es nula o vacía, inicializarla como una lista vacía
        if (horarios == null || horarios.isEmpty()) {
            horarios = Collections.emptyList();
        }

        // Convertir los horarios en un formato más simple para JavaScript
        List<Map<String, String>> horariosList = horarios.stream().map(horario -> Map.of(
                "id", horario.getId().toString(),
                "date", horario.getDia().toString(),
                "time", horario.getTime().toString(),
                "status", horario.getIdestadoCita().getEstado()
        )).collect(Collectors.toList());

        // Convertir la lista a JSON
        String horariosJson = objectMapper.writeValueAsString(horariosList);
        log.info("Horarios JSON: {}", horariosJson);
        model.addAttribute("medico", medico.getNombre());
        model.addAttribute("idmedico", medico.getIdMedico());
        model.addAttribute("consultorio", consultorio);
        model.addAttribute("horarios", horariosJson);

        return "plantillaCalendario";
    }

    @GetMapping("/reservarCita")
    public String reservarCita(@RequestParam String doctor,
                               @RequestParam String consultorio,
                               @RequestParam String dia,
                               @RequestParam String hora,
                               @RequestParam String idmedico,
                               @RequestParam String idestado,
                               @RequestParam String idhorario,
                               Model model, RedirectAttributes redirectAttributes) {

        // Procesar los parámetros recibidos
        LocalDate fecha = LocalDate.parse(dia, DateTimeFormatter.ISO_DATE);
        LocalTime tiempo = LocalTime.parse(hora, DateTimeFormatter.ISO_TIME);
        Long idDoctor = Long.parseLong(idmedico);
        Long idEstado = Long.parseLong(idestado);

        Horarios horario = horarioServiceImpl.obtenerHorario(Long.parseLong(idhorario));
        EstadoCita estadoCita = new EstadoCita();
        estadoCita.setIdestadoCita(idEstado);

        // Actualizar el estado del horario
        if (horario != null) {
            horario.setIdestadoCita(estadoCita); // Establece el nuevo estado de la cita
            horarioServiceImpl.guardarHorario(horario);
            redirectAttributes.addFlashAttribute("success", "Cita reservada exitosamente");
            return "redirect:/paginaConfirmacionCita?doctor=" + doctor + "&consultorio=" + consultorio +
                    "&dia=" + dia + "&hora=" + hora + "&idmedico=" + idmedico + "&idestado=" + idestado;
        } else {
            model.addAttribute("mensaje", "Horario no encontrado");
            return "resultadoReserva"; // Devuelve la vista con el mensaje de error
        }
    }

    // Método para manejar la página de confirmación
    @GetMapping("/paginaConfirmacionCita")
    public String paginaConfirmacionCita(@RequestParam String doctor,
                                         @RequestParam String consultorio,
                                         @RequestParam String dia,
                                         @RequestParam String hora,
                                         @RequestParam String idmedico,
                                         @RequestParam String idestado,
                                         Model model) {
        model.addAttribute("doctor", doctor);
        model.addAttribute("consultorio", consultorio);
        model.addAttribute("dia", dia);
        model.addAttribute("hora", hora);
        model.addAttribute("idmedico", idmedico);
        model.addAttribute("estado", idestado);

        return "paginaConfirmacionCita"; // Nombre de la vista Thymeleaf que se debe renderizar
    }

    @GetMapping("/paginaHistorialCitadoPorMedico")
    public String paginaHistorialCitadoPorMedico(Model model) {
        return "paginaHistorialCitadoPorMedico";
    }
}


