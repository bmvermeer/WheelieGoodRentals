package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Booking;
import nl.brianvermeer.demo.wheeliegoodrentals.service.BookingService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class HomeController {

    private final BookingService bookingService;

    public HomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping({"/", "/index", "/home"})
    public String getAllRentals(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("view", "home");
        return "layout";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("view", "login");
        return "layout";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("view", "signup");
        return "layout";
    }

    @GetMapping("/terms")
    public String getTermsOfUse(Model model) {
        Path termsPath = Paths.get("documents/terms-of-use.txt");
        try {
            String termsContent = Files.readString(termsPath);
            model.addAttribute("termsContent", termsContent);
        } catch (IOException e) {
            model.addAttribute("termsContent", "Unable to load terms of use.");
        }
        model.addAttribute("view", "terms");
        return "layout";
    }

    @GetMapping("/terms/download")
    public ResponseEntity<Resource> downloadTermsOfUse() {
        Path termsPath = Paths.get("documents/terms-of-use.txt");
        Resource resource = new FileSystemResource(termsPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=terms-of-use.txt")
                .body(resource);
    }

    @GetMapping(value = "/notes.md", produces = "text/plain")
    @ResponseBody
    public Resource notes() {
        return new ClassPathResource("static/notes.md");
    }
}