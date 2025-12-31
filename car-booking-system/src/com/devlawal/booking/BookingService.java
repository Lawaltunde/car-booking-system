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

    // returns all bookings in database
    public Booking[] getAllBookings(){
        Booking[] theBookings =  bookingDao.getAllBookingsWithoutNull();
        if (theBookings.length == 0)
            return new Booking[0];
        return theBookings;
    }

    public Booking[] getAllBookingsWithFreeSlots(){
        return bookingDao.getAllBookings();
    }

    // adds a booking to database
    public UUID book(Booking booking) {
        if (booking == null) {
            System.out.println("wrong input");
            throw new IllegalArgumentException("wrong input");
        }
        Objects.requireNonNull(booking.getCar(), "car can't be null");
        Objects.requireNonNull(booking.getUser(), "user can't be null");
        Objects.requireNonNull(booking.getBookingId(), "booking id can't be null");
        Objects.requireNonNull(booking.getBookingTime(), "booking time can't be null");
        Objects.requireNonNull(booking.getUser().getId(), "user id can't be null");

//        Booking[] allTheBookings = getAllBookingsWithFreeSlots();
//        if ( allTheBookings[0] == null) {
//            boolean isAdded =  bookingDao.addBooking(booking);
//            if (isAdded) {
//                System.out.println("Booking added");
//                return booking.getBookingId();
//            }
//        }
        for (Booking allBooking : getAllBookings()) {
            if(allBooking.getCar().getId().equals(booking.getCar().getId())){
                System.out.println("Car already booked");
                throw new IllegalArgumentException("Car already booked");
            }

        }
        boolean isAdded =  bookingDao.addBooking(booking);
        if (isAdded) {
            System.out.println("Booking added");
            return booking.getBookingId();
        }

//        for (Booking theBooking : allTheBookings) {
//            if (theBooking != null && theBooking.getCar().getId().equals(booking.getCar().getId())){
//                System.out.println("Car already booked");
//                throw new IllegalArgumentException("Car already booked");
//            }
//            if (theBooking != null && theBooking.getUser().getId().equals(booking.getUser().getId())){
//                System.out.println("User already booked");
//                throw new IllegalArgumentException("User already booked");
//            }
//        }
//        boolean isAdded = bookingDao.addBooking(booking);
        return isAdded ? booking.getBookingId() : null;
    }

    // checks if user is booked
    public Booking checkIfUserIsBooked(UUID id){
        User aUser = userService.getUserById(id);
        if (getAllBookings().length == 0) {
            throw new IllegalArgumentException("No bookings yet for " + aUser);
        }
        if (id == null) {
            System.out.println("Wrong input");
            throw new IllegalArgumentException("Wrong input, id can't be null");
        }

        for (Booking booking : getAllBookings()) {
            if (booking.getUser().getId().equals(id)) {
                return booking;
            }
        }
        return null;
    }

    // returns all available cars in database
    public Car[] getAllAvailableCars()
    {
        Car[] allCars = carService.getAllCars();
        return getCars(allCars);
    }

    // returns all available electric cars in database
    public Car[] getAllAvailableElectricCar()
    {
        Car[] allElectricCarsCars = carService.getAllElectricCars();
        return getCars(allElectricCarsCars);
    }

    // use to get all available cars and it used in getAllAvailableCars and getAllAvailableElectricCar
    private Car[] getCars(Car[] candidateCars) {
        Booking[] allBookings = getAllBookings();
        if (candidateCars == null || candidateCars.length == 0)
            return new Car[0];
        if (allBookings.length == 0)
            return candidateCars;

        int index = 0;
        for (Car car : candidateCars) {
            boolean booked = false;
            for (Booking booking : allBookings) {
                if (booking == null || booking.getCar() == null || booking.getCar().getId() == null)
                    continue;
                if (car.getId().equals(booking.getCar().getId())) {
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                index++;
            }
        }
        Car[] availableCars = new Car[candidateCars.length - index];
        int availableIndex = 0;
        for (Car aCar : candidateCars) {
            if (aCar == null)
                continue;
            boolean booked = false;
            for (Booking booking : allBookings) {
                if (booking == null || booking.getCar() == null || booking.getCar().getId() == null)
                    continue;
                if (aCar.getId().equals(booking.getCar().getId())) {
                    booked = true;
                    break;
                }
            }
            if (!booked && availableIndex < availableCars.length) {
                availableCars[availableIndex++] = aCar;
            }
        }
        return availableCars;
    }
}
