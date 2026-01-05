# Amigoscode Academy Car Booking System Project

A small, educational Java command-line application that demonstrates a simple car booking system using in-memory arrays.

The project uses plain Java (no external libraries) and stores data in arrays (not Lists or a database), so it's lightweight and easy to inspect.

---

## Key Features

- Browse available cars and electric cars
- Book a car for a user
- View a user's booked car
- View all bookings
- Simple, console-based interactive menu


## Project Structure

```
car-booking-system/
├─ src/
│  ├─ Main.java                # CLI entrypoint
│  └─ com/devlawal/
│     ├─ car/                  # Car model, DAO, and service (array-backed)
│     ├─ user/                 # User model, DAO, and service (array-backed)
│     └─ booking/              # Booking model, DAO, and service (array-backed)
├─ README.md
```

Notes:
- Data is stored in plain arrays (intentionally). This keeps the code simple and focused on algorithmic logic rather than collections or persistence.
- Packages follow a clear separation of concerns: models, DAOs (data access), and services (business logic).

---

## Getting Started (macOS / zsh)

1. Open a terminal and cd into the repository root (where `src/` is located):

   ```bash
   cd /path/to/car-booking-system
   ```

2. Compile all Java sources into an `out/` directory (this command works on macOS zsh):

   ```bash
   mkdir -p out && javac -d out $(find src -name "*.java")
   ```

3. Run the program from the project root:

   ```bash
   java -cp out Main
   ```

4. Use the numeric menu to interact with the application (e.g., `1` to book a car, `7` to exit).

---

## Example Interaction

When you run the app, you'll see a menu like:

1️⃣ - Book Car
2️⃣ - View All User Booked Cars
3️⃣ - View All Bookings
4️⃣ - View Available Cars
5️⃣ - View Available Electric Cars
6️⃣ - View all users
7️⃣ - Exit

Enter the number for a command and follow the prompts.

---

## Design Notes & Implementation Details

- Storage
  - The system uses arrays to hold data for cars, users, and bookings.
  - This is intentional for learning purposes. Arrays make the implementation explicit and show how to manage fixed-size storage manually.

- IDs
  - Users, cars, and bookings use UUIDs to uniquely identify entities.

- Error handling
  - The CLI does basic input validation; more robust validation could be added.

- No external dependencies
  - Everything runs with the standard JDK.

---

## License

This project is provided for learning purposes. For now it is not intended for production use, maybe in the future it will be licensed under MIT or similar.

---




