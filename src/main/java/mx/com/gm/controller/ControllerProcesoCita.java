package mx.com.gm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
public class ControllerProcesoCita {

    @GetMapping("/company")
    public String company(Model model) {
        return "company";
    }

    @GetMapping("/service")
    public String service(Model model) {
        return "service";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        return "blog";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        return "gallery";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @GetMapping("/pagina1")
    public String pagina1(Model model) {
        return "cita1";
    }

    @GetMapping("/seleccionar")
    public String seleccionarEspecialidad(@RequestParam("valor") String valor, Model model) {
        model.addAttribute("valor", valor);
        log.info("valor: " + valor);
        return "cita2";
    }

    @GetMapping("/seleccionar2")
    public String seleccionarMedico(@RequestParam("valor") String valor, Model model) {
        model.addAttribute("valor", valor);
        log.info("valor: " + valor);
        return "pagina3";
    }
    

}
