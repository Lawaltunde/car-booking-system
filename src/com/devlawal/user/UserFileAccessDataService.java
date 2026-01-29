package src.com.devlawal.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
        List<User>localUsers = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalStateException("User file not found: " + filePath);
        }
        List<User> users = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                    if (parts.length != 3) {
                        // skip lines
                        // I will modify this later to maybe add random id to a data missing id
                        continue;
                    }
                    try {
                        String name = parts[0].trim();
                        String email = parts[1].trim();
                        String ageStr = parts[2].trim();
                        int age = Integer.parseInt(ageStr);

                        localUsers.add(new User(name, email, age));
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
            }
            return localUsers;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
