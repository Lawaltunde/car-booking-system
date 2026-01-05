package com.devlawal.booking;

import com.devlawal.car.Car;
import com.devlawal.user.User;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Booking {
    private UUID bookingId;
    private LocalDateTime bookingTime;
    private Car car;
    private User user;
    private boolean isBooked;


    public Booking(UUID bookingId, LocalDateTime bookingTime, Car car, User user) {
        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.car = car;
        this.user = user;
        this.isBooked = false;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
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


    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingTime=" + bookingTime +
                ", car=" + car +
                ", user=" + user +
                ", bookingCanceled=" + isBooked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return isBooked == booking.isBooked && Objects.equals(bookingId, booking.bookingId) && Objects.equals(bookingTime, booking.bookingTime) && Objects.equals(car, booking.car) && Objects.equals(user, booking.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, bookingTime, car, user, isBooked);
    }
}
