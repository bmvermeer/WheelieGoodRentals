package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MessageUtil {

    public static void addSuccessMessage(Model model, String message) {
        model.addAttribute("message", message);
        model.addAttribute("messageType", "success");
    }

    public static void addErrorMessage(Model model, String message) {
        model.addAttribute("message", message);
        model.addAttribute("messageType", "error");
    }

    public static void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("messageType", "success");
    }

    public static void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("messageType", "error");
    }
}
