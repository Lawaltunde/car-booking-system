package src.com.devlawal;

import src.com.devlawal.booking.Booking;
import src.com.devlawal.booking.BookingService;
import src.com.devlawal.car.Car;
import src.com.devlawal.car.CarService;
import src.com.devlawal.user.User;
import src.com.devlawal.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class MainService {

    public static int inputErrorHandler() {
        int opt = 0;
        printMenu();
        try {
            Scanner scanner = new Scanner(System.in);
            opt = scanner.nextInt();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
        } catch (Exception e) {
            e.getStackTrace();
        }
        return opt;
    }

    public static void userInput(UserService userService, BookingService bookingService, CarService carService) {
        boolean currentStatus = true;
        while (currentStatus) {
            int opt = inputErrorHandler();
            switch (opt) {
                case 1:
                    for (Car car : carService.getAllCars()) {
                        System.out.println(car);
                    }
                    System.out.println("Select the car's id you will like to book");
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String id = scanner.nextLine().trim();
                        Car car = null;
                        for (Car vehicle : carService.getAllCars()) {
                            if (vehicle != null && vehicle.getRegNumber().equals(id)) {
                                car = vehicle;
                                break;
                            }
                        }
                        for (User user : userService.getAllUsers()) {
                            System.out.println(user);
                        }
                        System.out.println("Select the user's id you will like use");
                        Scanner scannerUser = new Scanner(System.in);
                        String userId = scanner.nextLine().trim();
                        UUID theUserId = UUID.fromString(userId);
                        User user = userService.getUserById(theUserId);
                        LocalDateTime bookingTime = LocalDateTime.now();
                        Booking booking = new Booking(bookingTime, car, user);
                        bookingService.bookCar(booking);
                        System.out.println("Booking was successful for " + booking.getUser() +
                                " for " + booking.getCar() + " on " + booking.getBookingTime() + "with booking id: " + booking.getBookingId() + "!");
                    } catch (Exception e) {
                        System.out.println("Booking was unsuccessful " + e);
                    }
                    break;
                case 2:
                    List<User> theUsers = userService.getAllUsers();
                    if (theUsers.isEmpty()) {
                        System.out.println("No users yet, sorry!");
                    }
                    theUsers.forEach(System.out::println);
                    System.out.println("Select the user's id you will like to checked");
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String id = scanner.nextLine().trim();
                        UUID theId = UUID.fromString(id);
                        User thatUser = userService.getUserById(theId);
                        if (thatUser == null) {
                            System.out.println("User with id " + id + " not found in database");
                            break;
                        }
                        Booking booking = bookingService.checkBookedUser(theId);
                        if (booking == null) {
                            System.out.println(thatUser + " has no car booked yet");
                        } else {
                            System.out.println(thatUser + " has been booked for: " + booking.getCar() + " on " + booking.getBookingTime());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    List<Booking> bookings = bookingService.getAllBookings();
                    if (bookings.isEmpty()) {
                        System.out.println("No bookings yet, sorry!");
                    }
                    bookings.forEach(System.out::println);
                    break;
                case 4:
                    List<Car> allAvailableCars = bookingService.getAllBookedCar();
                    if (allAvailableCars.isEmpty()) {
                        System.out.println("No cars available, sorry!");
                    }
                    allAvailableCars.forEach(System.out::println);
                    break;
                case 5:
                    List<Car> allAvailableElectricCar = bookingService.getAllBookedElectricCar();
                    if (allAvailableElectricCar.isEmpty()) {
                        System.out.println("No electric cars available, sorry!");
                    }
                    allAvailableElectricCar.forEach(System.out::println);
                    break;
                case 6:
                    List<User> users = userService.getAllUsers();
                    users.forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    currentStatus = false;
                    break;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }

    public static void printMenu() {
        String menu = """
                1️⃣ - Book Car
                2️⃣ - View All User Booked Cars
                3️⃣ - View All Bookings
                4️⃣ - View Available Cars
                5️⃣ - View Available Electric Cars
                6️⃣ - View all users
                7️⃣ - Exit
                """;
        System.out.println(menu);
    }
}
