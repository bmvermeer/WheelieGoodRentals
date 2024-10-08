package nl.brianvermeer.demo.wheeliegoodrentals;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Role;
import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class WheelieGoodRentalsApplication {

    private final UserService userService;
    private final Filler filler;
    private static final Logger logger = LoggerFactory.getLogger(WheelieGoodRentalsApplication.class);

    public WheelieGoodRentalsApplication(UserService userService, Filler filler) {
        this.userService = userService;
        this.filler = filler;
    }

    public static void main(String[] args) {
       SpringApplication.run(WheelieGoodRentalsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        filler.fill();

        List<User> users = userService.getAllUsers();
        users.forEach(x -> logger.info(x.toString()));
        logger.info("READY");
    }
}