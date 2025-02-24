package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ErrorController {

    @GetMapping({"/403"})
    public String getAllRentals(Model model) {
        model.addAttribute("view", "403");
        return "layout";
    }

}
