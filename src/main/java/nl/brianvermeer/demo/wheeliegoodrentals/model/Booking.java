package nl.brianvermeer.demo.wheeliegoodrentals.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    private String bookingReference;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(nullable = false)
    private double price;

    // Constructors
    public Booking() {
    }

    public Booking(String bookingReference, LocalDate startDate, LocalDate endDate, Car car, User user, double price) {
        this.bookingReference = bookingReference;
        this.startDate = startDate;
        this.endDate = endDate;
        this.car = car;
        this.user = user;
        this.price = price;
    }

    // Getters and setters
    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        return bookingReference != null ? bookingReference.equals(booking.bookingReference) : booking.bookingReference == null;
    }

    @Override
    public int hashCode() {
        return bookingReference != null ? bookingReference.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingReference='" + bookingReference + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", car=" + car +
                ", user=" + user +
                ", price=" + price +
                '}';
    }
}