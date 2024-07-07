package mx.com.gm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ControladorHome {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/patient")
    public String patient() {
        return "doctor";
    }

}
