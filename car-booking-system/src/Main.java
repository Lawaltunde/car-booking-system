import com.devlawal.booking.Booking;
import com.devlawal.booking.BookingService;
import com.devlawal.car.Car;
import com.devlawal.car.CarService;
import com.devlawal.user.User;
import com.devlawal.user.UserService;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;
import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
       BookingService bookingService = new BookingService();
       UserService userService = new UserService();
       CarService carService = new CarService();

        printMenu();
        userInput(userService, bookingService, carService);
    }

    private static int inputErrorHandler() {
        int opt = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            opt = scanner.nextInt();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
            //throw new IllegalArgumentException("Invalid input. Please enter a number between 1 and 7.");
      } catch (Exception e) {
            //System.out.println("Invalid input. Please enter a number between 1 and 7.");
            e.getStackTrace();
        }
        return opt;

    }

    private static void userInput(UserService userService, BookingService bookingService, CarService carService) {
        int opt = inputErrorHandler();
        while (opt <= 7) {
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
                            if (vehicle != null && vehicle.getId().equals(id)){
                                car = vehicle;
                                break;
                            }
                        }
                        for (User user : userService.getOnlyAvailableUser()) {
                            System.out.println(user);
                        }
                        System.out.println("Select the user's id you will like use");
                        Scanner scannerUser = new Scanner(System.in);
                        String userId = scanner.nextLine().trim();
                        UUID theUserId = UUID.fromString(userId);
                        User user = userService.getUserById(theUserId);
                        UUID bookingId = UUID.randomUUID();
                        LocalDateTime bookingTime = LocalDateTime.now();
                        Booking booking = new Booking(bookingId, bookingTime, car, user);
                        bookingService.book(booking);
                        System.out.println("Booking was successful for " + booking.getUser() +
                                " for " + booking.getCar() + " on " + booking.getBookingTime() + "with booking id: " + booking.getBookingId() + "!");
                    }catch (Exception e) {
                        System.out.println("Booking was unsuccessful " + e);
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 2:
                    User[] user = userService.getOnlyAvailableUser();
                    if (user.length == 0)
                        System.out.println("No users yet, sorry!");
                    for (User u : user) {
                        System.out.println(u);
                    }
                    System.out.println("Select the user's id you will like to be checked");
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String id = scanner.nextLine().trim();
                        UUID theId = UUID.fromString(id);
                        User thatUser = userService.getUserById(theId);
                        if (thatUser == null) {
                            System.out.println("User with id " + id + " not found in database");
                            printMenu();
                            opt = inputErrorHandler();
                            break;
                        }
                        Booking booking = bookingService.checkIfUserIsBooked(theId);
                        if (booking == null)
                            System.out.println( thatUser + " has no car booked yet");
                        else
                            System.out.println(thatUser + " has been booked for: " + booking.getCar() + " on " + booking.getBookingTime());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 3:
                    Booking[] bookings = bookingService.getAllBookings();
                    if (bookings.length == 0)
                        System.out.println("No bookings yet, sorry!");
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 4:
                    for (Car car : bookingService.getAllAvailableCars()) {
                        System.out.println(car);
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 5:
                    for (Car electricCar: bookingService.getAllAvailableElectricCar()) {
                        System.out.println(electricCar);
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 6:
                    for (User user1 : userService.getOnlyAvailableUser()) {
                        System.out.println(user1);
                    }
                    printMenu();
                    opt = inputErrorHandler();
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    exit(99);
                    break;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    printMenu();
                    opt = inputErrorHandler();
            }
        }
    }

    static void printMenu() {
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