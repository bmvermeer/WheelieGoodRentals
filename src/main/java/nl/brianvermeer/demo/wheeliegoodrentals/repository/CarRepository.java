package nl.brianvermeer.demo.wheeliegoodrentals.repository;

import nl.brianvermeer.demo.wheeliegoodrentals.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}