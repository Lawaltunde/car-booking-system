package com.devlawal.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class UserFakerDataAccessService implements UserDao {
    private static List<User> users;
    private static Faker faker;

    static {
        faker = new Faker();
        users = userFromJaveFakerDependency();
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
        return true;
    }

    static List<User> userFromJaveFakerDependency() {
        List<User> fakeUsers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String name = faker.name().firstName();
            String email = name + "@drive.com";
            int age = (i % 2) + 20;
            User user = new User(name, email, age);
            fakeUsers.add(user);
            user = null;
        }
        return fakeUsers;
    }
}
