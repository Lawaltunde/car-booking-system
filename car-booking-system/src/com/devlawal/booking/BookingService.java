package com.devlawal.booking;

import com.devlawal.car.Car;
import com.devlawal.car.CarService;
import com.devlawal.user.User;
import com.devlawal.user.UserService;

import java.util.Objects;
import java.util.UUID;

public class BookingService {
    private static final BookingDao bookingDao;
    private static final CarService carService;
    private static final UserService userService;

    static {
        bookingDao = new BookingDao();
        carService = new CarService();
        userService = new UserService();
    }

    // returns all bookings in a database
    public Booking[] getAllBookings() {
        Booking[] theBookings = bookingDao.getAllBookings();
        if (theBookings.length == 0) {
            return new Booking[0];
        }
        return theBookings;
    }

    // adds a booking to database
    public UUID bookCar(Booking booking) {
        if (booking == null) {
            System.out.println("wrong input");
            throw new IllegalArgumentException("wrong input");
        }
        Objects.requireNonNull(booking.getCar(), "car can't be null");
        Objects.requireNonNull(booking.getUser(), "user can't be null");
        Objects.requireNonNull(booking.getBookingId(), "booking id can't be null");
        Objects.requireNonNull(booking.getBookingTime(), "booking time can't be null");
        Objects.requireNonNull(booking.getUser().getId(), "user id can't be null");

        for (Booking abooking : getAllBookings()) {
            if (abooking.getCar().getRegNumber().equals(booking.getCar().getRegNumber())) {
                System.out.println("Car already booked, please choose another car or check back later!");
                throw new IllegalArgumentException("Car already booked");
            }
            if (!booking.getUser().isAvailable()) {
                System.out.println("User is not available, please choose another user or check back later!");
                throw new IllegalArgumentException("User is not available");
            }

        }
        boolean isBooked = bookingDao.addBooking(booking);
        if (!isBooked) {
            System.out.println("Booking failed");
            throw new IllegalArgumentException("Booking failed");
        }
        System.out.println("Booking added");
        booking.getUser().setAvailable(false);
        booking.getCar().setAvailable(false);
        return booking.getBookingId();
    }

    // The below methods are used to check users, cars and electrical cars inside a booking database.
    // checks if a user is on the booking list
    public Booking checkBookedUser(UUID id) {
        User aUser = userService.getUserById(id);
        if (getAllBookings().length == 0) {
            throw new IllegalArgumentException("No bookings yet!");
        }
        if (id == null) {
            System.out.println("Id can't be null!");
            throw new IllegalArgumentException("Wrong input, id can't be null");
        }

        for (Booking booking : getAllBookings()) {
            if (booking.getUser().getId().equals(id)) {
                return booking;
            }
        }
        throw new IllegalArgumentException("User is not on the booking list");
    }

    // returns all available cars in a database
    public Car[] getAllBookedCar() {
        Car[] allCars = carService.getAllCars();
        return getBookedCar(allCars);
    }

    // returns all available electric cars in database
    public Car[] getAllBookedElectricCar() {
        Car[] allElectricCarsCars = carService.getAllElectricCars();
        return getBookedCar(allElectricCarsCars);
    }

    // use to get all available cars and it used to getAllAvailableCars and getAllAvailableElectricCar
    private Car[] getBookedCar(Car[] candidateCars) {
        Booking[] allBookings = getAllBookings();
        if (candidateCars == null || candidateCars.length == 0) {
            return new Car[0];
        }
        if (allBookings.length == 0) {
            return candidateCars;
        }

        int index = 0;
        for (Car car : candidateCars) {
            boolean booked = false;
            for (Booking booking : allBookings) {
                if (booking == null || booking.getCar() == null || booking.getCar().getRegNumber() == null) {
                    continue;
                }
                if (car.getRegNumber().equals(booking.getCar().getRegNumber()) && booking.isBooked()) {
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                index++;
            }
        }
        Car[] car = new Car[index];
        int availableIndex = 0;
        for (Car aCar : candidateCars) {
            if (aCar == null) {
                continue;
            }
            boolean booked = false;
            for (Booking booking : allBookings) {
                if (booking == null || booking.getCar() == null || booking.getCar().getRegNumber() == null) {
                    continue;
                }
                if (aCar.getRegNumber().equals(booking.getCar().getRegNumber()) && booking.isBooked()) {
                    booked = true;
                    break;
                }
            }
            if (!booked && availableIndex < car.length) {
                car[availableIndex++] = aCar;
            }
        }
        return car;
    }

}
