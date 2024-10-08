package nl.brianvermeer.demo.wheeliegoodrentals.repository;

import nl.brianvermeer.demo.wheeliegoodrentals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}