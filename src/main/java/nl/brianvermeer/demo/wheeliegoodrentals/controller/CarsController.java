package nl.brianvermeer.demo.wheeliegoodrentals.controller;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Car;
import nl.brianvermeer.demo.wheeliegoodrentals.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CarsController {

    private final CarService carService;

    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public String getAllCars(Model model) {
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
        model.addAttribute("view", "cars");
        return "layout";
    }

    @PostMapping("/cars")
    public String searchCars(Model model, @RequestParam String input) {
        List<Car> cars = carService.searchCars(input);
        model.addAttribute("cars", cars);
        model.addAttribute("view", "cars");
        return "layout";
    }
}