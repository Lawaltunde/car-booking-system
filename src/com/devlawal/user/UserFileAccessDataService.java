package src.com.devlawal.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
public class UserFileAccessDataService implements UserDao {
    private static List<User> users;


    static {
        users = getUsersFromFile("src/com/devlawal/users.csv");
    }

    @Override
    public List<User> getUsers() {
        return users;
    }


    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        users.add(user);
        // I will modify this later to probably return id
        return true;
    }

    private static List<User> getUsersFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalStateException("User file not found: " + filePath);
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line != null && !line.trim().isEmpty()) {
                    line = scanner.nextLine().trim();
                    String[] parts = line.split(",");
                    if (parts.length != 3) {
                        // skip lines
                        // I will modify this later to maybe add random id to a data missing id
                        continue;
                    }
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    String ageStr = parts[2].trim();
                    int age;
                    try {
                        age = Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        // skip lines with invalid age, I don't want anyone below 18 to drive
                        System.out.println(e.getMessage());
                        continue;
                    }
                    users.add(new User(name, email, age));
                }
            }
            return users;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
