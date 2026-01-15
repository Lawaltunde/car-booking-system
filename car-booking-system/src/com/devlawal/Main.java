package com.devlawal;

import com.devlawal.booking.BookingDao;
import com.devlawal.booking.BookingService;
import com.devlawal.car.CarDao;
import com.devlawal.car.CarService;
import com.devlawal.user.UserArrayAccessDataService;
import com.devlawal.user.UserDao;
import com.devlawal.user.UserFileAccessDataService;
import com.devlawal.user.UserService;
import static com.devlawal.MainService.userInput;

public class Main {
    public static void main(String[] args) {
        UserDao fileUserDao = new UserFileAccessDataService();
        UserDao arrayUserDao = new UserArrayAccessDataService();
        UserService userService = new UserService(fileUserDao, arrayUserDao);
        CarDao carDao = new CarDao();
        CarService carService = new CarService(carDao);
        BookingDao bookingDao = new BookingDao();
        BookingService bookingService = new BookingService(bookingDao, carService, userService);
        userInput(userService, bookingService, carService);
    }
}