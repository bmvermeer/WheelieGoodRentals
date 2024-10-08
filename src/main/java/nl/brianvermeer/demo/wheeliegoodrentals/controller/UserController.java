package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("view", "users");
        return "layout";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("user with username " + userDetails.getUsername() + " not found"));
        model.addAttribute("user", user);
        model.addAttribute("view", "profile");

        // Get list of uploaded files
        String uploadDir = "upload/" + user.getUsername();
        try {
            List<String> files = Files.list(Paths.get(uploadDir))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
            model.addAttribute("files", files);
        } catch (IOException e) {
            model.addAttribute("files", List.of());
        }
        return "layout";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name, @RequestParam String email, @RequestParam String phone, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("user with username " + userDetails.getUsername() + " not found"));
        user.setUsername(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        userService.updateUser(user);
        return "redirect:/profile?success";
    }

    @PostMapping("/profile/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) {
        if (file.isEmpty()) {
            return "redirect:/profile?error";
        }

        User user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("user with username " + userDetails.getUsername() + " not found"));
        String uploadDir = "upload/" + user.getUsername();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            var name = file.getOriginalFilename().replace(" ", "_");
            var fileNameAndPath = Paths.get(uploadDir, name);
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/profile?error";
        }

        return "redirect:/profile?success";
    }

    @PostMapping("/profile/delete")
    public String deleteFile(@RequestParam("fileName") String fileName, Model model) {
        Path filePath = Paths.get("upload", fileName);
        try {
            Files.deleteIfExists(filePath);
            model.addAttribute("message", "File deleted successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to delete file.");
        }
        return "redirect:/profile";
    }



}