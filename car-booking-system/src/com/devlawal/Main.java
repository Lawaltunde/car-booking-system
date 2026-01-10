package com.devlawal;

import com.devlawal.booking.BookingService;
import com.devlawal.car.CarService;
import com.devlawal.user.UserService;


import static com.devlawal.MainService.printMenu;
import static com.devlawal.MainService.userInput;

public class Main {
    public static void main(String[] args) {
        BookingService bookingService = new BookingService();
        UserService userService = new UserService();
        CarService carService = new CarService();


        printMenu();
        userInput(userService, bookingService, carService);
    }
}