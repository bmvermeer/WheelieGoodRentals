package nl.brianvermeer.demo.wheeliegoodrentals.service;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Booking;
import nl.brianvermeer.demo.wheeliegoodrentals.model.Car;
import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import nl.brianvermeer.demo.wheeliegoodrentals.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(User user, Car car, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        double price = days * car.getPricePerDay() + 1;
        Booking booking = new Booking(generateBookingReference(), startDate, endDate, car, user, price);
        return bookingRepository.save(booking);
    }

    private String generateBookingReference() {
        // Generate a unique booking reference
        return UUID.randomUUID().toString();
    }

    public Booking getBookingByReference(String reference) {
        return bookingRepository.findById(reference).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(String reference) {
        bookingRepository.deleteById(reference);
    }

    public List<Booking> getAllBookingsForUser(User user) {
        return bookingRepository.findAllByUser(user);
    }

}