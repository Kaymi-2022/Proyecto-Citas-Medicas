package mx.com.gm.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.com.gm.domain.HistorialCitados;
import mx.com.gm.domain.Usuario;
import mx.com.gm.servicio.HistorialCitasService;
import mx.com.gm.servicio.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private UsuarioServiceImpl userService;

    @Autowired
    private HistorialCitasService historialService;


    @GetMapping("/citados")
    public String tableCitados(Model model, @AuthenticationPrincipal User user) {
        log.info("ejecutando el controlador Spring MVC");
        log.info("usuario que hizo login:" + user);
        var historial = historialService.listarHistorialPaciente();
        model.addAttribute("citas", historial);
        return "table-citados";
    }

}