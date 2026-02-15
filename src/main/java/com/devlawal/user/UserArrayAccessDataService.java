package com.devlawal.user;

import java.util.ArrayList;
import java.util.List;

public class UserArrayAccessDataService implements UserDao {
    private final List<User> users;

    public UserArrayAccessDataService() {
        this.users = new ArrayList<>();
        initializeUsers();
    }

    private void initializeUsers() {
        users.add(new User("Ademola", "Ademola@drive.com", 29));
        users.add(new User("Akande", "Akande@drive.com", 27));
        users.add(new User("Joshua", "Joshua@drive.com", 28));
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
}
