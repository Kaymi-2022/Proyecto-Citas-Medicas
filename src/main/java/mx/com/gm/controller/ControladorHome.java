package mx.com.gm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import mx.com.gm.domain.Usuario;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ControladorHome {

    @GetMapping("/")
    public String index() {
        log.info("Ejecutando el controlador Spring MVC");
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        log.info("Ejecutando el controlador Spring MVC");
        return "index";
    }

    @GetMapping("/company")
    public String company(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        return "paginacompany";
    }

    @GetMapping("/service")
    public String service(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        return "paginaservice";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        return "paginablog";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        return "paginagallery";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        log.info("Ejecutando el controlador Spring MVC");
        return "paginacontact";
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("situacion", "error");
        }
        log.info("Ejecutando el controlador Spring MVC");
        return "login";
    }

    @GetMapping("/patient")
    public String patient() {
        log.info("Ejecutando el controlador Spring MVC");
        return "doctor";
    }

    @GetMapping("/agregar")
    public String agregar(Usuario usuario) {
        log.info("Ejecutando el controlador Spring MVC");
        return "modificar";
    }

}
