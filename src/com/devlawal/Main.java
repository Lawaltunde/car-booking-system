package src.com.devlawal;


import src.com.devlawal.booking.BookingDao;
import src.com.devlawal.booking.BookingService;
import src.com.devlawal.car.CarDao;
import src.com.devlawal.car.CarService;
import src.com.devlawal.user.UserArrayAccessDataService;
import src.com.devlawal.user.UserDao;
import src.com.devlawal.user.UserFileAccessDataService;
import src.com.devlawal.user.UserService;

import static src.com.devlawal.MainService.userInput;

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

