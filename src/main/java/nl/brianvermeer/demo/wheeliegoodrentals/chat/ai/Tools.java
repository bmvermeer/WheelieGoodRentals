package nl.brianvermeer.demo.wheeliegoodrentals.chat.ai;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Car;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Role;
import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.service.BookingService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.CarService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;


public class Tools {

    private CarService carService;
    private UserService userService;
    private BookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    public Tools(CarService carService, UserService userService, BookingService bookingService) {
        this.carService = carService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @Tool("Get the current date and time")
    public String currentTime(Object a) {
        logger.warn("CALLED FUNCTION currentTime {}", a);
        var currentTimeMS = System.currentTimeMillis();
        var currentTime = new java.util.Date(currentTimeMS);
        return currentTime.toString();
    }

    @Tool
    public String availableCars(Object a) {
        logger.warn("CALLED FUNCTION availableCars {}", a);
        List<Car> cars = carService.getAllCars();
        return cars.stream().map(Car::toString).reduce("", (x, y) -> x + y + "\n");
    }

    @Tool("Get the details of a user")
    public String getUserByUserName(@P("Username of the user") String userName) {
        logger.warn("CALLED FUNCTION getUserByUserName {}", userName);
        var user = userService.getUserByUsername(userName).orElseThrow( () -> new IllegalArgumentException("User not found"));
        return user.toString();
    }

    @Tool("Book a car for a user")
    public String newBooking(@P("Username of the user") String username, @P("Id of the car to rent")Long carId, @P("StartDate of the booking") LocalDate startDate, @P("StartDate of the booking") LocalDate endDate) {
        logger.warn("CALLED FUNCTION newBooking {} {} {} {}", username, carId, startDate,endDate);
        var user = userService.getUserByUsername(username).orElseThrow( () -> new IllegalArgumentException("User not found"));
        var car = carService.getCarById(carId).orElseThrow( () -> new IllegalArgumentException("Car not found"));
        var booking = bookingService.createBooking(user, car, startDate, endDate);
        return booking.toString();
    }

    @Tool("find a booking by reference")
    public String findBooking(@P("Id of the booking to find") String bookingReference) {
        logger.warn("CALLED FUNCTION findBooking {}", bookingReference);
        var booking = bookingService.getAllBookings().stream().filter(b -> b.getBookingReference().equals(bookingReference)).findFirst().orElseThrow( () -> new IllegalArgumentException("Booking not found"));
        return booking.toString();
    }

    @Tool("Delete a booking")
    public void deleteBooking(@P("Id of the booking to delete") String bookingReference, @P("username of the user") String username) {
        logger.warn("CALLED FUNCTION deleteBooking {} {}",bookingReference, username);
        var user = userService.getUserByUsername(username).orElseThrow( () -> new IllegalArgumentException("User not found"));
        var booking = bookingService.getBookingByReference(bookingReference);
        if (booking.getUser().getId().equals(user.getId())) {
            bookingService.deleteBooking(bookingReference);
        } else {
            throw new IllegalArgumentException("Booking does not belong to user " + username);
        }
    }

    @Tool("Create a new user")
    public User createNewUser(@P("Username for the new user") String username, @P("The password for the new User") String password, @P("The email of the new User") String email, @P("The phonenumber for the new User") String phonenumber) {
        logger.warn("CALLED FUNCTION createNewUser {} {} {} {}",username, password, email, phonenumber);
        return userService.createUser(username, password, email, phonenumber, Role.USER);
    }
}
