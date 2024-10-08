package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Booking;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Role;
import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.service.BookingService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/bookings")
    public String getAllRentals(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("user with username " + userDetails.getUsername() + " not found"));
        List<Booking> bookings;
        if (user.getRole() == Role.ADMIN) {
            bookings = bookingService.getAllBookings();
        }
        bookings = bookingService.getAllBookingsForUser(user);

        model.addAttribute("bookings", bookings);
        model.addAttribute("view", "bookings");
        return "layout";
    }
}