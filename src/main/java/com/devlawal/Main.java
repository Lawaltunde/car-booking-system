package com.devlawal;

import com.devlawal.booking.BookingDao;
import com.devlawal.booking.BookingService;
import com.devlawal.car.CarDao;
import com.devlawal.car.CarService;
import com.devlawal.user.*;

import static com.devlawal.MainService.userInput;

public class Main {
    public static void main(String[] args) {
        // Choose ONE data source for users
        // Options: UserFileAccessDataService, UserArrayAccessDataService, UserFakerDataAccessService
        UserDao userDao = selectUserDataSource();
        
        // Initialize services with dependency injection
        UserService userService = new UserService(userDao);
        CarDao carDao = new CarDao();
        CarService carService = new CarService(carDao);
        BookingDao bookingDao = new BookingDao();
        BookingService bookingService = new BookingService(bookingDao, carService, userService);

        userInput(userService, bookingService, carService);
    }

    private static UserDao selectUserDataSource() {
        // Change this to use different data sources:
        // return new UserArrayAccessDataService();
        // return new UserFileAccessDataService();
        return new UserFakerDataAccessService();
    }
}

