package mx.com.gm.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import mx.com.gm.domain.*;
import mx.com.gm.repository.HorariosDao;
import mx.com.gm.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ControladorAdmin {


    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;

    @Autowired
    private HistorialCitasServiceImpl historialServiceimpl;

    @Autowired
    private EspecialidadServiceInpl especialidadServiceInpl;
    @Autowired
    private MedicoServiceImpl medicoServiceImpl;
    
    @Autowired
    private HorariosServiceImpl HorariosServiceImpl;
    
    @Autowired
    private EstadoServiceImpl estadoServiceImpl;
    
    @Autowired
    private HorariosDao horariosDao;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;
    

    @GetMapping("/table-principal")
    public String tableadmin(Model model, @AuthenticationPrincipal User user) {
        var users = usuarioServiceImpl.findAllUsersWithRoleNames();
        var usersadmin = usuarioServiceImpl.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = usuarioServiceImpl.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = usuarioServiceImpl.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("usuariosConRoles", users);
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        log.info("usuario que hizo login:" + user);
        return "table-usuarios";
    }

    @GetMapping("/table-citados")
    public String tableCitados(Model model, @AuthenticationPrincipal User user) {
        log.info("usuario que hizo login:" + user);
        var historial = historialServiceimpl.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }


    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Usuario usuario, Errors errores, Model model) {
        String respuesta = null;
        var usersadmin = usuarioServiceImpl.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = usuarioServiceImpl.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = usuarioServiceImpl.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
            return "redirect:/table-principal";
        } else {
            usuarioServiceImpl.guardar(usuario);
            respuesta = "success";
            var users = usuarioServiceImpl.findAllUsersWithRoleNames();
            model.addAttribute("usuariosConRoles", users);
            model.addAttribute("situacion", respuesta);
            return "table-usuarios";
        }
    }

    @GetMapping("/eliminarUsuario")
    public String eliminarUsuario(Model model, Usuario usuario, Errors errores) {
        String respuesta = null;
        var usersadmin = usuarioServiceImpl.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = usuarioServiceImpl.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = usuarioServiceImpl.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
            return "redirect:/table-principal";
        } else {
            usuarioServiceImpl.eliminar(usuario);
            respuesta = "success";
            var users = usuarioServiceImpl.findAllUsersWithRoleNames();
            model.addAttribute("usuariosConRoles", users);
            model.addAttribute("situacion", respuesta);
            return "table-usuarios";
        }
    }

    @PostMapping("/register")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam String dni,
                                   @RequestParam String celular,
                                   @RequestParam String correo,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   Model model) {

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setCelular(celular);
        usuario.setCorreo(correo);
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));

        String respuesta = null;

        Usuario usuarioRegistrado = usuarioServiceImpl.registrarUsuario(usuario);
        if (usuarioRegistrado != null) {
            respuesta = "successRegister";
            model.addAttribute("situacion", respuesta);
        } else {
            respuesta = "errorRegister";
            model.addAttribute("situacion", respuesta);
        }
        return "login";
    }

    @GetMapping("/paginaGraficoEstadicticos")
    public String getTotalCitadosPorEspecialidad(Model model) {
        // Datos de citados por especialidad
        List<HistorialCitasServiceImpl.EspecialidadTotalDTO> especialidadData = historialServiceimpl
                .getTotalCitadosByEspecialidad();
        List<String> especialidadLabels = especialidadData.stream()
                .map(HistorialCitasServiceImpl.EspecialidadTotalDTO::getEspecialidad)
                .collect(Collectors.toList());
        List<Long> especialidadTotals = especialidadData.stream()
                .map(HistorialCitasServiceImpl.EspecialidadTotalDTO::getTotal)
                .collect(Collectors.toList());

        // Datos de situación por consultorio
        List<HistorialCitasServiceImpl.SituacionCountDTO> situacionData = historialServiceimpl.getSituacionCount();
        List<String> situacionLabels = situacionData.stream()
                .map(HistorialCitasServiceImpl.SituacionCountDTO::getSituacion)
                .collect(Collectors.toList());
        List<Long> situacionTotals = situacionData.stream()
                .map(HistorialCitasServiceImpl.SituacionCountDTO::getTotal)
                .collect(Collectors.toList());

        // Datos de conteo por mes
        List<HistorialCitasServiceImpl.MesCountDTO> mesData = historialServiceimpl.getCountByMonth();
        List<String> fechaLabels = mesData.stream()
                .map(HistorialCitasServiceImpl.MesCountDTO::getMes)
                .collect(Collectors.toList());
        List<Long> fechaTotals = mesData.stream()
                .map(HistorialCitasServiceImpl.MesCountDTO::getTotal)
                .collect(Collectors.toList());

        // Agregar datos al modelo
        model.addAttribute("especialidadLabels", especialidadLabels);
        model.addAttribute("especialidadTotals", especialidadTotals);
        model.addAttribute("situacionLabels", situacionLabels);
        model.addAttribute("situacionTotals", situacionTotals);
        model.addAttribute("fechaLabels", fechaLabels);
        model.addAttribute("fechaTotals", fechaTotals);

        return "paginaGraficoEstadicticos";
    }

    @GetMapping("/eliminarCitado")
    public String eliminarCitado(Model model, HistorialCitados historialCitados, Errors errores) {
        String respuesta = null;
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
        } else {
            historialServiceimpl.eliminar(historialCitados);
            respuesta = "success";
            model.addAttribute("situacion", respuesta);
        }
        var historial = historialServiceimpl.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }

    @PostMapping("/guardarPaciente")
    public String guardarPaciente(@Valid HistorialCitados historialCitados, Errors errores, Model model) {
        String respuesta = null;
        if (errores.hasErrors()) {
            respuesta = "error";
            log.info("Error al eliminar" + errores.getAllErrors());
            model.addAttribute("situacion", respuesta);
        } else {
            historialServiceimpl.guardar(historialCitados);
            respuesta = "success";
            model.addAttribute("situacion", respuesta);

        }
        var historial = historialServiceimpl.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }

    @GetMapping("/table-consultorios")
    public String tableConsultorio(Especialidad especialidad, RedirectAttributes flash, Model model) {
        model.addAttribute("consultorios", especialidadServiceInpl.listarConsultorios());
        return "table-consultorios";
    }

    @PostMapping("/guardarConsultorio")
    public String guardarConsultorio(@RequestParam(required = false) MultipartFile file,
                                     @Valid Especialidad especialidad, BindingResult result,
                                     RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Hay errores en el formulario");
            log.info("Error al guardar" + result.getAllErrors());
            return "redirect:/table-consultorios";
        }

        if (file != null && !file.isEmpty()) {
            String ruta = "E://ProyectoCitasHospital/src/main/resources/static/img/fotos/";

            try {
                byte[] bytes = file.getBytes();
                Path rutaAbsoluta = Paths.get(ruta + file.getOriginalFilename());

                // Asegurarse de que el directorio exista
                Files.createDirectories(rutaAbsoluta.getParent());

                Files.write(rutaAbsoluta, bytes);
                especialidad.setFoto(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                flash.addFlashAttribute("error", "Error subiendo la foto");
                return "redirect:/table-consultorios";
            }
        }

        especialidadServiceInpl.guardar(especialidad);
        flash.addFlashAttribute("success", "Consultorio registrado con éxito");
        return "redirect:/table-consultorios";
    }

    @GetMapping("/eliminarConsultorio")
    public String eliminarConsultorio(@Valid Especialidad especialidad, RedirectAttributes flash) {
        especialidadServiceInpl.eliminar(especialidad);
        flash.addFlashAttribute("success", "Consultorio eliminado con éxito");
        return "redirect:/table-consultorios";
    }

    @GetMapping("/table-doctores")
    public String tableDoctores(Model model) {
        model.addAttribute("consultorios", especialidadServiceInpl.listarConsultorios());
        model.addAttribute("medicos", medicoServiceImpl.listarMedicos());
        return "table-doctores";
    }

    @PostMapping("/guardarMedico")
    public String tableDoctores(@RequestParam(required = false) MultipartFile file,
                                @RequestParam Long idConsultorio,
                                @Valid Medicos medicos, BindingResult result,
                                RedirectAttributes flash) {
      /*  if (result.hasErrors()) {

            flash.addFlashAttribute("error", "Hay errores en el formulario");
            log.info("Error al guardar: " + idConsultorio + " " + result.getAllErrors());
            return "redirect:/table-doctores";
        }*/

        // Obtener el consultorio por su ID
        Optional<Especialidad> consultorioOpt = especialidadServiceInpl.obtenerConsultorioPorId(idConsultorio);
        if (!consultorioOpt.isPresent()) {
            flash.addFlashAttribute("error", "Consultorio no encontrado");
            return "redirect:/table-doctores";
        }

        Especialidad especialidad = consultorioOpt.get();
        medicos.setEspecialidad(especialidad);

        if (file != null && !file.isEmpty()) {
            String ruta = "E://ProyectoCitasHospital/src/main/resources/static/img/fotos/";

            try {
                byte[] bytes = file.getBytes();
                Path rutaAbsoluta = Paths.get(ruta + file.getOriginalFilename());

                // Asegurarse de que el directorio exista
                Files.createDirectories(rutaAbsoluta.getParent());

                Files.write(rutaAbsoluta, bytes);
                medicos.setFoto(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                log.info("Error al guardar: " + e.getMessage());
                flash.addFlashAttribute("error", "Error subiendo la foto");
                return "redirect:/table-doctores";
            }
        }

        medicoServiceImpl.guardar(medicos);
        flash.addFlashAttribute("success", "Medico registrado con éxito");
        return "redirect:/table-doctores";
    }

    @GetMapping("/eliminarMedico")
    public String eliminarMedico(Medicos medicos, RedirectAttributes flash) {
        medicoServiceImpl.eliminar(medicos);
        flash.addFlashAttribute("success", "Medico eliminado con éxito");
        return "redirect:/table-doctores";
    }

    @GetMapping("/table-horarios")
    public String tableHorarios(Model model) {
        List<Horarios> horariosList = HorariosServiceImpl.listarHorarios();

        // Añadir un día a cada fecha en la lista de horarios
        horariosList = horariosList.stream().map(h -> {
            h.setDia(h.getDia().plusDays(1));
            return h;
        }).collect(Collectors.toList());

        model.addAttribute("medicos", medicoServiceImpl.listarMedicos());
        model.addAttribute("estadosCita", estadoServiceImpl.listarEstados());
        model.addAttribute("horarios", horariosList);

        return "table-horarios";
    }

    @PostMapping("/guardarHorario")
    public String guardarHorario(@Valid Horarios horario, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Hay errores en el formulario");
            log.info("Error al guardar: " + result.getAllErrors());
            return "redirect:/table-horarios";
        }

        log.info("Horario recibido: " + horario.getDia());

        // Guarda el horario
        horariosDao.save(horario);

        flash.addFlashAttribute("success", "Horario registrado con éxito");
        return "redirect:/table-horarios";
    }
}