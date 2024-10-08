package nl.brianvermeer.demo.wheeliegoodrentals.service;

import jakarta.persistence.EntityManager;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Car;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final EntityManager em;

    public CarService(CarRepository carRepository, EntityManager em) {
        this.carRepository = carRepository;
        this.em = em;
    }

    public Car createCar(String licensePlate, String model, String manufacturer, double pricePerDay) {
        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setModel(model);
        car.setBrand(manufacturer);
        car.setPricePerDay(pricePerDay);

        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car updateCar(Long id, String licensePlate, String model, String manufacturer, double pricePerDay) {
        Optional<Car> carOptional = carRepository.findById(id);
        if (!carOptional.isPresent()) {
            throw new IllegalArgumentException("Car not found");
        }

        Car car = carOptional.get();
        car.setLicensePlate(licensePlate);
        car.setModel(model);
        car.setBrand(manufacturer);
        car.setPricePerDay(pricePerDay);

        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> searchCars(String input) {
        var lowerInput = input.toLowerCase(Locale.ROOT);
        String query = "Select * from Car where lower(brand) like '%" + lowerInput + "%' OR lower(model) like '%" + lowerInput + "%'";
        var resultList = (List<Car>) em.createNativeQuery(query, Car.class).getResultList();
        return resultList;
    }
}