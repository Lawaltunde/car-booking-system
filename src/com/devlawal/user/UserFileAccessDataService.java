package src.com.devlawal.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

public class UserFileAccessDataService implements UserDao {
    private static User[] users;


    static {
        users = getUsersFromFile("src/com/devlawal/users.csv");
    }

    @Override
    public User[] getUsers() {
        return users;
    }


    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }

        if (users == null || users.length == 0) {
            users = new User[5];
            users[0] = user;
            return true;
        }

        int index = 0;
        for (User existing : users) {
            if (existing == null) {
                users[index] = user;
                return true;
            }
            index++;
        }

        // No null slot found, an array is full. Expand and append the user.
        User[] moreUsers = new User[users.length + 5];
        System.arraycopy(users, 0, moreUsers, 0, users.length);
        moreUsers[users.length] = user;
        users = moreUsers;
        return true;
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("id can't be null");
        }

        if (users == null || users.length == 0) {
            // nothing to delete
            return;
        }

        int pos = -1;
        for (int i = 0; i < users.length; i++) {
            User user = users[i];
            if (user != null && user.getId() != null && user.getId().equals(userId)) {
                pos = i;
                users[i] = null;
                break;
            }
        }

        if (pos == -1) {
            // not found, there is nothing to delete
            return;
        }

        // shift left to remove the null hole
        for (int i = pos; i < (users.length - 1); i++) {
            users[i] = users[i + 1];
        }
        users[users.length - 1] = null;
    }

    private static User[] getUsersFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new User[0];
        }

        try {
            // First pass: count non-empty lines
            int count = 0;
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line != null && !line.trim().isEmpty()) {
                        count++;
                    }
                }
            }

            if (count == 0) {
                return new User[0];
            }

            User[] usersFromFile = new User[count];
            int index = 0;

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length != 3) {
                        // skip lines
                        continue;
                    }
                    String name = parts[0].trim();
                    String email = parts[1].trim();
                    String ageStr = parts[2].trim();
                    int age;
                    try {
                        age = Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        // skip lines with invalid age
                        System.out.println(e.getMessage());
                        continue;
                    }

                    usersFromFile[index++] = new User(name, email, age);
                }
            }

            // if lines are skipped, the index may be less than count. then I need to shrink the array
            if (index != usersFromFile.length) {
                User[] newUsers = new User[index];
                System.arraycopy(usersFromFile, 0, newUsers, 0, index);
                return newUsers;
            }

            return usersFromFile;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
