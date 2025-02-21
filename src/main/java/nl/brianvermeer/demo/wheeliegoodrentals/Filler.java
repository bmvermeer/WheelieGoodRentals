package nl.brianvermeer.demo.wheeliegoodrentals;

import net.datafaker.Faker;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.ChatMessage;
import nl.brianvermeer.demo.wheeliegoodrentals.chat.Conversation;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Role;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.ChatMessageRepository;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.ConversationRepository;
import nl.brianvermeer.demo.wheeliegoodrentals.service.BookingService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.CarService;
import nl.brianvermeer.demo.wheeliegoodrentals.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class Filler {

    private BookingService bookingService;
    private CarService carService;
    private UserService userService;
    private ConversationRepository conversationRepository;
    private ChatMessageRepository chatMessageRepository;
    private Faker faker;

    public Filler(BookingService bookingService, CarService carService, UserService userService, ConversationRepository conversationRepository, ChatMessageRepository chatMessageRepository) {
        this.bookingService = bookingService;
        this.carService = carService;
        this.userService = userService;
        this.faker = new Faker();
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public void fill() {
        createStaticData();
        createUsers(10);
//        createCars(30);
        createFiftyCars();
        createConversation();
    }

    public void createStaticData() {
        var user = userService.createUser("brian", "brian", "brian@brianvermeer.nl", "+31612345678", Role.USER);
        var car = carService.createCar("1-ABC-123", "Model S", "Tesla", 160.0);
        bookingService.createBooking(user, car, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), "abc-123");
        bookingService.createBooking(user, car, LocalDate.now().plusDays(15), LocalDate.now().plusDays(20), "xyz-321");

        userService.createUser("admin", "admin", "admin@admin.nl", "+31612345678", Role.ADMIN);
    }

    public void createUser() {
        userService.createUser(faker.name().username(), faker.internet().password(), faker.internet().emailAddress(), faker.phoneNumber().cellPhone(), Role.USER);
    }

    public void createUsers(int amount) {
        for (int i = 0; i < amount; i++) {
            createUser();
        }
    }

    public void createCar() {
        carService.createCar(faker.vehicle().licensePlate(), faker.vehicle().model(), faker.vehicle().manufacturer(), faker.number().randomDouble(2, 50, 200));
    }

    public void createCars(int amount) {
        for (int i = 0; i < amount; i++) {
            createCar();
        }
    }

    public void createFiftyCars() {
        Faker faker = new Faker();
        carService.createCar(faker.bothify("?-###-###"), "Aventador", "Lamborghini", 250.0);
        carService.createCar(faker.bothify("?-###-###"), "Civic", "Honda", 75.0);
        carService.createCar(faker.bothify("?-###-###"), "Corolla", "Toyota", 70.0);
        carService.createCar(faker.bothify("?-###-###"), "Mustang", "Ford", 150.0);
        carService.createCar(faker.bothify("?-###-###"), "Camry", "Toyota", 80.0);
        carService.createCar(faker.bothify("?-###-###"), "Accord", "Honda", 85.0);
        carService.createCar(faker.bothify("?-###-###"), "Model 3", "Tesla", 140.0);
        carService.createCar(faker.bothify("?-###-###"), "Altima", "Nissan", 75.0);
        carService.createCar(faker.bothify("?-###-###"), "Charger", "Dodge", 130.0);
        carService.createCar(faker.bothify("?-###-###"), "Impala", "Chevrolet", 90.0);
        carService.createCar(faker.bothify("?-###-###"), "Fusion", "Ford", 85.0);
        carService.createCar(faker.bothify("?-###-###"), "Malibu", "Chevrolet", 80.0);
        carService.createCar(faker.bothify("?-###-###"), "Sentra", "Nissan", 70.0);
        carService.createCar(faker.bothify("?-###-###"), "Jetta", "Volkswagen", 75.0);
        carService.createCar(faker.bothify("?-###-###"), "Passat", "Volkswagen", 85.0);
        carService.createCar(faker.bothify("?-###-###"), "A4", "Audi", 120.0);
        carService.createCar(faker.bothify("?-###-###"), "3 Series", "BMW", 130.0);
        carService.createCar(faker.bothify("?-###-###"), "C-Class", "Mercedes-Benz", 140.0);
        carService.createCar(faker.bothify("?-###-###"), "E-Class", "Mercedes-Benz", 160.0);
        carService.createCar(faker.bothify("?-###-###"), "S60", "Volvo", 110.0);
        carService.createCar(faker.bothify("?-###-###"), "XC90", "Volvo", 150.0);
        carService.createCar(faker.bothify("?-###-###"), "Q5", "Audi", 130.0);
        carService.createCar(faker.bothify("?-###-###"), "X5", "BMW", 150.0);
        carService.createCar(faker.bothify("?-###-###"), "GLC", "Mercedes-Benz", 140.0);
        carService.createCar(faker.bothify("?-###-###"), "CX-5", "Mazda", 90.0);
        carService.createCar(faker.bothify("?-###-###"), "RAV4", "Toyota", 95.0);
        carService.createCar(faker.bothify("?-###-###"), "CR-V", "Honda", 100.0);
        carService.createCar(faker.bothify("?-###-###"), "Escape", "Ford", 85.0);
        carService.createCar(faker.bothify("?-###-###"), "Equinox", "Chevrolet", 80.0);
        carService.createCar(faker.bothify("?-###-###"), "Rogue", "Nissan", 90.0);
        carService.createCar(faker.bothify("?-###-###"), "Tiguan", "Volkswagen", 95.0);
        carService.createCar(faker.bothify("?-###-###"), "Outback", "Subaru", 100.0);
        carService.createCar(faker.bothify("?-###-###"), "Forester", "Subaru", 95.0);
        carService.createCar(faker.bothify("?-###-###"), "Highlander", "Toyota", 120.0);
        carService.createCar(faker.bothify("?-###-###"), "Pilot", "Honda", 110.0);
        carService.createCar(faker.bothify("?-###-###"), "Explorer", "Ford", 130.0);
        carService.createCar(faker.bothify("?-###-###"), "Traverse", "Chevrolet", 125.0);
        carService.createCar(faker.bothify("?-###-###"), "Pathfinder", "Nissan", 120.0);
        carService.createCar(faker.bothify("?-###-###"), "Atlas", "Volkswagen", 130.0);
        carService.createCar(faker.bothify("?-###-###"), "Q7", "Audi", 150.0);
        carService.createCar(faker.bothify("?-###-###"), "X7", "BMW", 160.0);
        carService.createCar(faker.bothify("?-###-###"), "GLS", "Mercedes-Benz", 170.0);
        carService.createCar(faker.bothify("?-###-###"), "XC60", "Volvo", 140.0);
        carService.createCar(faker.bothify("?-###-###"), "Macan", "Porsche", 180.0);
        carService.createCar(faker.bothify("?-###-###"), "Cayenne", "Porsche", 200.0);
        carService.createCar(faker.bothify("?-###-###"), "Model X", "Tesla", 190.0);
        carService.createCar(faker.bothify("?-###-###"), "Model Y", "Tesla", 170.0);
        carService.createCar(faker.bothify("?-###-###"), "Range Rover", "Land Rover", 200.0);
        carService.createCar(faker.bothify("?-###-###"), "Defender", "Land Rover", 180.0);
        carService.createCar(faker.bothify("?-###-###"), "Discovery", "Land Rover", 160.0);
    }

    public void createConversation() {
        var conversation = new Conversation();
        conversationRepository.save(conversation);

        createChatMessage("user", "Hi can you please help me?", conversation);
        createChatMessage("Assistant", "Sure, I'd be happy to help! Could you please provide more details or specify what you need assistance with?", conversation);

    }

    private void createChatMessage(String sender, String content, Conversation conversation) {
        var chatMessage = new ChatMessage(sender, content, conversation);
        chatMessageRepository.save(chatMessage);
    }

}
