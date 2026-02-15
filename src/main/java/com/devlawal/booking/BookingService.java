package com.devlawal.booking;

import com.devlawal.car.Car;
import com.devlawal.car.CarService;
import com.devlawal.exception.BookingException;
import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import com.devlawal.user.User;
import com.devlawal.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    private final BookingDao bookingDao;
    private final CarService carService;
    private final UserService userService;

    public BookingService(BookingDao bookingDao, CarService carService, UserService userService) {
        this.bookingDao = bookingDao;
        this.carService = carService;
        this.userService = userService;
    }

    public List<Booking> getAllBookings() {
        return bookingDao.getAllBookings();
    }

    public UUID bookCar(Booking booking) {
        if (booking == null) {
            throw new ValidationException("Booking cannot be null");
        }
        
        if (booking.getCar() == null) {
            throw new ValidationException("Car cannot be null");
        }
        
        if (booking.getUser() == null) {
            throw new ValidationException("User cannot be null");
        }
        
        if (booking.getBookingId() == null) {
            throw new ValidationException("Booking ID cannot be null");
        }
        
        if (booking.getBookingTime() == null) {
            throw new ValidationException("Booking time cannot be null");
        }
        
        if (booking.getUser().getId() == null) {
            throw new ValidationException("User ID cannot be null");
        }

        boolean carAlreadyBooked = getAllBookings().stream()
                .anyMatch(existingBooking -> existingBooking.getCar() != null 
                        && existingBooking.getCar().getRegNumber() != null
                        && existingBooking.getCar().getRegNumber().equals(booking.getCar().getRegNumber()));

        if (carAlreadyBooked) {
            throw new BookingException(
                "Car with registration " + booking.getCar().getRegNumber() + 
                " is already booked. Please choose another car or check back later"
            );
        }

        if (!booking.getUser().isAvailable()) {
            throw new BookingException(
                "User " + booking.getUser().getName() + 
                " is not available. Please choose another user or check back later"
            );
        }

        boolean isBooked = bookingDao.addBooking(booking);
        if (!isBooked) {
            throw new BookingException("Failed to create booking. Please try again");
        }

        booking.getUser().setAvailable(false);
        booking.getCar().setAvailable(false);
        booking.setBooked(true);

        return booking.getBookingId();
    }

    public Booking checkBookedUser(UUID userId) {
        if (userId == null) {
            throw new ValidationException("User ID cannot be null");
        }

        User user = userService.getUserById(userId);

        List<Booking> allBookings = getAllBookings();
        if (allBookings.isEmpty()) {
            return null;
        }

        return allBookings.stream()
                .filter(booking -> booking.getUser() != null 
                        && booking.getUser().getId() != null
                        && booking.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public List<Car> getAllAvailableCars() {
        List<Car> allCars = carService.getAllCars();
        return getAvailableCars(allCars);
    }

    public List<Car> getAllAvailableElectricCars() {
        List<Car> allElectricCars = carService.getAllElectricCars();
        return getAvailableCars(allElectricCars);
    }

    private List<Car> getAvailableCars(List<Car> candidateCars) {
        if (candidateCars.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> allBookings = getAllBookings();
        if (allBookings.isEmpty()) {
            return candidateCars; // No bookings, all cars are available
        }

        Set<String> bookedCarRegNumbers = allBookings.stream()
                .filter(booking -> booking.getCar() != null && booking.getCar().getRegNumber() != null)
                .map(booking -> booking.getCar().getRegNumber())
                .collect(Collectors.toSet());

        return candidateCars.stream()
                .filter(car -> car.getRegNumber() != null 
                        && !bookedCarRegNumbers.contains(car.getRegNumber()))
                .toList();
    }
}
