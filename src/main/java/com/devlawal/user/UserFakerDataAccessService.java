package com.devlawal.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UserFakerDataAccessService implements UserDao {
    private final List<User> users;
    private final Faker faker;

    public UserFakerDataAccessService() {
        this.faker = new Faker();
        this.users = new ArrayList<>();
        initializeUsers();
    }

    private void initializeUsers() {
        //20 fake users
        List<User> fakeUsers = IntStream.range(0, 20)
                .mapToObj(i -> {
                    String name = faker.name().firstName();
                    String email = name.toLowerCase() + "@drive.com";
                    int age = (i % 2) + 20;
                    return new User(name, email, age);
                })
                .toList();
        
        users.addAll(fakeUsers);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users);
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
