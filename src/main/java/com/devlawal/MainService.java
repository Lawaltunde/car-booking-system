package com.devlawal;

import com.devlawal.booking.Booking;
import com.devlawal.booking.BookingService;
import com.devlawal.car.Car;
import com.devlawal.car.CarService;
import com.devlawal.exception.BookingException;
import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import com.devlawal.user.User;
import com.devlawal.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MainService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void userInput(UserService userService, BookingService bookingService, CarService carService) {
        boolean running = true;
        while (running) {
            printMenu();
            int option = getMenuOption();
            
            switch (option) {
                case 1 -> handleBookCar(carService, userService, bookingService);
                case 2 -> handleViewUserBooking(userService, bookingService);
                case 3 -> handleViewAllBookings(bookingService);
                case 4 -> handleViewAvailableCars(bookingService);
                case 5 -> handleViewAvailableElectricCars(bookingService);
                case 6 -> handleViewAllUsers(userService);
                case 7 -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
        scanner.close();
    }

    private static int getMenuOption() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
            scanner.nextLine(); // Clear the invalid input
            return -1;
        }
    }

    private static void handleBookCar(CarService carService, UserService userService, BookingService bookingService) {
        try {
            // Display all cars
            System.out.println("\n=== Available Cars ===");
            carService.getAllCars().forEach(System.out::println);
            
            // Select car
            System.out.println("\nEnter the car's registration number:");
            scanner.nextLine(); // Consume newline
            String carId = scanner.nextLine().trim();
            
            Car selectedCar = carService.getAllCars().stream()
                    .filter(car -> car.getRegNumber().equals(carId))
                    .findFirst()
                    .orElse(null);
            
            if (selectedCar == null) {
                System.out.println("❌ Car with registration " + carId + " not found!");
                return;
            }
            
            // Display all users
            System.out.println("\n=== Available Users ===");
            userService.getAllUsers().forEach(System.out::println);
            
            // Select user
            System.out.println("\nEnter the user's id:");
            String userId = scanner.nextLine().trim();
            UUID userUuid = UUID.fromString(userId);
            User selectedUser = userService.getUserById(userUuid);
            
            // Create booking
            LocalDateTime bookingTime = LocalDateTime.now();
            Booking booking = new Booking(bookingTime, selectedCar, selectedUser);
            bookingService.bookCar(booking);
            
            System.out.println("\n✅ Booking successful!");
            System.out.println("User: " + selectedUser.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " (" + selectedCar.getRegNumber() + ")");
            System.out.println("Time: " + bookingTime);
            System.out.println("Booking ID: " + booking.getBookingId());
            
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (BookingException e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("❌ Invalid input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid UUID format. Please enter a valid user ID.");
        } catch (Exception e) {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
            System.err.println("Error details: " + e.getClass().getSimpleName());
        }
    }

    private static void handleViewUserBooking(UserService userService, BookingService bookingService) {
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users available!");
                return;
            }
            
            System.out.println("\n=== All Users ===");
            users.forEach(System.out::println);
            
            System.out.println("\nEnter the user's id to check:");
            scanner.nextLine(); // Consume newline
            String userId = scanner.nextLine().trim();
            UUID userUuid = UUID.fromString(userId);
            
            User user = userService.getUserById(userUuid);
            Booking booking = bookingService.checkBookedUser(userUuid);
            
            if (booking == null) {
                System.out.println("\n" + user.getName() + " has no car booked yet.");
            } else {
                System.out.println("\n📅 Booking Details:");
                System.out.println("User: " + user.getName());
                System.out.println("Car: " + booking.getCar().getBrand() + " (" + booking.getCar().getRegNumber() + ")");
                System.out.println("Booked on: " + booking.getBookingTime());
            }
        } catch (ResourceNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid UUID format. Please enter a valid user ID.");
        } catch (Exception e) {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void handleViewAllBookings(BookingService bookingService) {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings yet!");
            return;
        }
        
        System.out.println("\n=== All Bookings ===");
        bookings.forEach(System.out::println);
    }

    private static void handleViewAvailableCars(BookingService bookingService) {
        List<Car> availableCars = bookingService.getAllAvailableCars();
        if (availableCars.isEmpty()) {
            System.out.println("\nNo cars available!");
            return;
        }
        
        System.out.println("\n=== Available Cars ===");
        availableCars.forEach(System.out::println);
    }

    private static void handleViewAvailableElectricCars(BookingService bookingService) {
        List<Car> availableElectricCars = bookingService.getAllAvailableElectricCars();
        if (availableElectricCars.isEmpty()) {
            System.out.println("\nNo electric cars available!");
            return;
        }
        
        System.out.println("\n=== Available Electric Cars ===");
        availableElectricCars.forEach(System.out::println);
    }

    private static void handleViewAllUsers(UserService userService) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("\nNo users available!");
            return;
        }
        
        System.out.println("\n=== All Users ===");
        users.forEach(System.out::println);
    }

    private static void printMenu() {
        String menu = """
                
                ╔════════════════════════════════════════╗
                ║      CAR BOOKING SYSTEM MENU          ║
                ╚════════════════════════════════════════╝
                1️⃣  - Book Car
                2️⃣  - View User's Booked Car
                3️⃣  - View All Bookings
                4️⃣  - View Available Cars
                5️⃣  - View Available Electric Cars
                6️⃣  - View All Users
                7️⃣  - Exit
                
                Enter your choice:""";
        System.out.print(menu + " ");
    }
}
