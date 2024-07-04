package mx.com.gm.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.com.gm.domain.HistorialCitados;
import mx.com.gm.domain.Persona;
import mx.com.gm.domain.Usuario;
import mx.com.gm.servicio.HistorialCitasService;
import mx.com.gm.servicio.HistorialCitasServiceImpl;
import mx.com.gm.servicio.PersonaService;
import mx.com.gm.servicio.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UsuarioServiceImpl userService;

    @Autowired
    private HistorialCitasService historialService;

    @Autowired
    private HistorialCitasServiceImpl historialServiceimpl;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/table-principal")
    public String tableadmin(Model model,@AuthenticationPrincipal User user) {
        var users = userService.findAllUsersWithRoleNames();
        var usersadmin = userService.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = userService.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = userService.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("usuariosConRoles", users);
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        log.info("usuario que hizo login:" + user);
        return "table-principal";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("situacion", "error");
        }
        return "login";
    }

    @GetMapping("/patient")
    public String patient() {
        return "doctor";
    }

    @GetMapping("/citados")
    public String tableCitados(Model model, @AuthenticationPrincipal User user) {
        log.info("usuario que hizo login:" + user);
        var historial = historialService.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }

    @GetMapping("/agregar")
    public String agregar(Persona persona) {
        return "modificar";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Persona persona, Errors errores, Model model) {
        String respuesta = null;
        var usersadmin = userService.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = userService.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = userService.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
            return "redirect:/table-principal";
        } else {
            personaService.guardar(persona);
            respuesta = "success";
            var users = userService.findAllUsersWithRoleNames();
            model.addAttribute("usuariosConRoles", users);
            model.addAttribute("situacion", respuesta);
            return "table-principal";
        }
    }

    @GetMapping("/eliminarUsuario")
    public String eliminarUsuario(Model model, Usuario usuario, Errors errores) {
        String respuesta = null;
        var usersadmin = userService.countUsersWithAdminRole("ROLE_ADMIN");
        var userspatient = userService.countUsersWithAdminRole("ROLE_PATIENT");
        var usersdoctor = userService.countUsersWithAdminRole("ROLE_DOCTOR");
        model.addAttribute("totaldoadmin", usersadmin);
        model.addAttribute("totalpatient", userspatient);
        model.addAttribute("totaldoctor", usersdoctor);
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
            return "redirect:/table-principal";
        } else {
            userService.eliminar(usuario);
            respuesta = "success";
            var users = userService.findAllUsersWithRoleNames();
            model.addAttribute("usuariosConRoles", users);
            model.addAttribute("situacion", respuesta);
            return "table-principal";
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

        Usuario usuarioRegistrado = userService.registrarUsuario(usuario);
        if (usuarioRegistrado != null) {
            respuesta = "successRegister";
            model.addAttribute("situacion", respuesta);
        } else {
            respuesta = "errorRegister";
            model.addAttribute("situacion", respuesta);
        }
        return "login";
    }

    @GetMapping("/charts")
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

        return "charts";
    }

    @GetMapping("/eliminarCitado")
    public String eliminarCitado(Model model, HistorialCitados historialCitados, Errors errores) {
        String respuesta = null;
        if (errores.hasErrors()) {
            respuesta = "error";
            model.addAttribute("situacion", respuesta);
        } else {
            historialService.eliminar(historialCitados);
            respuesta = "success";
            model.addAttribute("situacion", respuesta);
        }
        var historial = historialService.listarHistorialPaciente();
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
            historialService.guardar(historialCitados);
            respuesta = "success";
            model.addAttribute("situacion", respuesta);

        }
        var historial = historialService.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }

    /*
     * @GetMapping("/editar/{idPersona}")
     * public String editar(Persona persona, Model model) {
     * persona = personaService.encontrarPersona(persona);
     * model.addAttribute("persona", persona);
     * return "modificar";
     * }
     */

    /*
     * @GetMapping("/hola")
     * public String tableroAdmin(Model model, @AuthenticationPrincipal User user) {
     * // Extracción de las listas de usuarios y personas
     * var personas = personaService.listarPersonas();
     * // Registro de información
     * log.info("ejecutando el controlador Spring MVC");
     * log.info("usuario que hizo login:" + user);
     * // Añadir atributos al modelo
     * model.addAttribute("personas", personas);
     * return "index";
     * }
     */
}