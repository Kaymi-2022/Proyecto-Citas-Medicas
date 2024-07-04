package mx.com.gm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.gm.domain.*;
import mx.com.gm.servicio.EspecialidadServiceInpl;
import mx.com.gm.servicio.HistorialCitasServiceImpl;
import mx.com.gm.servicio.HorariosServiceImpl;
import mx.com.gm.servicio.MedicoServiceImpl;
import mx.com.gm.servicio.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
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

    @Autowired
    private UsuarioServiceImpl userDetailsService;

    @Autowired
    HistorialCitasServiceImpl historialCitasServiceImpl;



    @GetMapping("/inicioCita")
    public String pagina1(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        return "paginaInicioCita";
    }

    @GetMapping("/plantillaConsultorios")
    public String seleccionarEspecialidad(@RequestParam("valor") String valor, Model model,@AuthenticationPrincipal User user) {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        var listaconsultorios = especialidadServiceInpl.listarConsultorios();
        model.addAttribute("consultorios", listaconsultorios);
        model.addAttribute("valor", valor);
        return "paginaConsultorios";
    }

    @GetMapping("/plantillaMedicos")
    public String seleccionarConsultorio(@Valid Especialidad especialidad, Model model,@AuthenticationPrincipal User user) {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
        model.addAttribute("medicos", medicoServiceImpl.encontrarMedicoPorConsultorio(especialidad.getIdConsultorio()));
        model.addAttribute("nombreConsultorio", especialidad.getNombre());
        return "paginaMedicos";
    }


    @GetMapping("/obtenerHorariosCalendario")
    public String getCalendar(@RequestParam(name = "consultorio") String consultorio, Medicos medico, Model model,@AuthenticationPrincipal User user) throws JsonProcessingException {
        model.addAttribute("usuario", userDetailsService.getNombre(user.getUsername()));
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

        return "paginaCalendario";
    }

    @GetMapping("/PaginaCitasPaciente")
    public String paginaCitasPaciente(Model model, @AuthenticationPrincipal User user) {
        Usuario usuario = userDetailsService.getUsuario(user.getUsername());
        model.addAttribute("citas", historialCitasServiceImpl.getHistorialCitas(usuario.getDni()));
        return "paginaCitasPaciente";
    }

    @GetMapping("/reservarCita")
    public String reservarCita(@RequestParam String idestado,
                               @RequestParam String idhorario,
                               RedirectAttributes redirectAttributes) {
    
        // Procesar los parámetros recibidos
        Long idEstado = Long.parseLong(idestado);
        Long idhHorario = Long.parseLong(idhorario);
    
        Optional<Horarios> optionalHorarios = horarioServiceImpl.obtenerHorarioPorId(idhHorario);
    
        // Actualizar el estado del horario
        if (optionalHorarios.isPresent()) {
            Horarios horarios = optionalHorarios.get();
            EstadoCita estadoCita = new EstadoCita();
            estadoCita.setIdestadoCita(idEstado);
    
            // Suponiendo que Horarios tiene un método para establecer el estado de la cita
            horarios.setIdestadoCita(estadoCita);
            horarioServiceImpl.guardarHorario(horarios);
    
            redirectAttributes.addFlashAttribute("success", "Cita reservada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Horario no encontrado");
        }
        return "redirect:/ConfirmacionCita?idhorario=" + idhorario;
    }

    // Método para manejar la página de confirmación
    @GetMapping("/ConfirmacionCita")
    public String paginaConfirmacionCita(@RequestParam String idhorario, Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        // Procesar los parámetros recibidos
        Usuario usuario = userDetailsService.getUsuario(user.getUsername());
        Long idhorarioLong = Long.parseLong(idhorario);
        Optional<Horarios> optionalHorario = horarioServiceImpl.obtenerHorarioPorId(idhorarioLong);
    
        if (optionalHorario.isPresent()) {
            Horarios horario = optionalHorario.get();
            HistorialCitados historialCitados = new HistorialCitados();
            historialCitados.setId(null);
            historialCitados.setFecha(horario.getDia());
            historialCitados.setHora(horario.getTime());
            historialCitados.setMedico(horario.getMedicos().getNombre());
            historialCitados.setConsultorio(horario.getMedicos().getEspecialidad().getNombre());
            historialCitados.setSituacion(horario.getIdestadoCita().getEstado());
            historialCitados.setActividad("Consulta Medica");
            historialCitados.setPaciente(usuario.getNombre());
            historialCitados.setDni(usuario.getDni());
            historialCitados.setEmail(usuario.getCorreo());
            historialCitados.setIdHorario(idhorario);
            historialCitasServiceImpl.guardar(historialCitados);
            redirectAttributes.addFlashAttribute("idHorario", horario.getId());
            model.addAttribute("citas", historialCitasServiceImpl.getHistorialCitas(usuario.getDni()));
        } else {
            redirectAttributes.addFlashAttribute("error", "Horario no encontrado");
            return "redirect:/obtenerHorariosCalendario";
        }
        return "redirect:/PaginaCitasPaciente"; 
    }

    @GetMapping("/cancelarCita")
    public String cancelarCita(HistorialCitados historialCitados, RedirectAttributes redirectAttributes, @RequestParam Long idHorario) {
        // Create and initialize EstadoCita with default idestadoCita value
        EstadoCita estadoCita = new EstadoCita();
        estadoCita.setIdestadoCita(1L);
    
        // Set the estadoCita in Horarios
        Horarios horarios = new Horarios();
        horarios.setIdestadoCita(estadoCita);
    
        // Update the estadoCita of the Horarios
        int resp = horarioServiceImpl.actualizarEstadoHorario(idHorario, horarios.getIdestadoCita().getIdestadoCita());
        if (resp == 0) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita");
            return "redirect:/PaginaCitasPaciente";
        }
    
        // Remove the historialCitados entry
        historialCitasServiceImpl.eliminar(historialCitados);
        redirectAttributes.addFlashAttribute("mensaje", "Cita cancelada exitosamente");
        return "redirect:/PaginaCitasPaciente";
    }

}


