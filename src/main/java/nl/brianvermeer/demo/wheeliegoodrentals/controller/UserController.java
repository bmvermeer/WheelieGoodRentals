package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static nl.brianvermeer.demo.wheeliegoodrentals.controller.MessageUtil.addErrorMessage;
import static nl.brianvermeer.demo.wheeliegoodrentals.controller.MessageUtil.addSuccessMessage;

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
                    .toList();
            model.addAttribute("files", files);
        } catch (IOException e) {
            addErrorMessage(model, "Failed to load files.");
            model.addAttribute("files", List.of());
        }
        return "layout";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name, @RequestParam String email, @RequestParam String phone, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("user with username " + userDetails.getUsername() + " not found"));
        user.setUsername(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        userService.updateUser(user);
        addSuccessMessage(redirectAttributes, "Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            addErrorMessage(redirectAttributes, "Please select a file to upload.");
            return "redirect:/profile";
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
            addErrorMessage(redirectAttributes, "Failed to upload file " + file.getOriginalFilename());
            return "redirect:/profile";
        }

        addSuccessMessage(redirectAttributes, file.getOriginalFilename() + " uploaded successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteFile(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes) {
        Path filePath = Paths.get("upload", fileName);

        try {
            Files.deleteIfExists(filePath);
            addSuccessMessage(redirectAttributes, filePath.getFileName() + " deleted successfully!");
        } catch (IOException e) {
            addErrorMessage(redirectAttributes, "Failed to delete file " + filePath.getFileName());
        }
        return "redirect:/profile";
    }

    //delete user
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("view", "home");


        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            addSuccessMessage(model, "You do not have permission to delete users.");
            return "layout";
        }

        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            addSuccessMessage(model, "User deleted successfully!");
        } else {
            addErrorMessage(model, "User not found.");
        }

        return "layout";
    }



}