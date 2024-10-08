package nl.brianvermeer.demo.wheeliegoodrentals.repository;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Booking;
import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findAllByUser(User user);
}