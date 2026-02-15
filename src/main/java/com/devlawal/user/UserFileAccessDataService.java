package com.devlawal.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserFileAccessDataService implements UserDao {
    private final List<User> users;
    private final String filePath;

    public UserFileAccessDataService() {
        this.filePath = getClass().getClassLoader().getResource("users.csv").getPath();
        this.users = new ArrayList<>();
        loadUsersFromFile();
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users); // Return defensive copy
    }

    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        users.add(user);
        return true;
    }

    private void loadUsersFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalStateException("File not found: " + filePath);
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                parseAndAddUser(line);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Failed to load users from file: " + filePath, e);
        }
    }

    private void parseAndAddUser(String line) {
        if (line == null || line.isBlank()) {
            return;
        }

        String[] parts = line.split(",");
        if (parts.length != 3) {
            // Skip invalid lines
            return;
        }

        try {
            String name = parts[0].trim();
            String email = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            users.add(new User(name, email, age));
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format in line: " + line);
        }
    }
}
