package com.devlawal.user;

import java.util.Objects;

public class UserService {
    private final UserDao userDao = new UserDao();

    // all users are retuned including the empty slots (nulls)
    public User[] getAllUsers(){
        return userDao.getUsers();
    }

    public User[] getOnlyAvailableUser() {
        // Use arrays only: count non-null entries, allocate a right-sized array, copy
        User[] all = getAllUsers();
        if (all == null || all.length == 0) return new User[0];

        int count = 0;
        for (User theUser : all) {
            if (theUser != null) count++;
        }

        User[] available = new User[count];
        int idx = 0;
        for (User theUser : all) {
            if (theUser != null) {
                available[idx++] = theUser;
            }
        }

        return available;
    }

    public boolean addUser(User user){
        // user can't be null
        Objects.requireNonNull(user, "user can't be null");

        Objects.requireNonNull(user.getAge(), "user age can't be null");
        if (user.getAge() < 18)
            throw new IllegalArgumentException("user must be at least 18 years old");

        Objects.requireNonNull(user.getName(), "user name can't be null");
        Objects.requireNonNull(user.getEmail(), "user email can't be null");
        Objects.requireNonNull(user.getId(), "user id can't be null");

        for (User aUser : getAllUsers()) {
            if (aUser != null && aUser.getId() != null && aUser.getId().equals(user.getId()))
                throw new IllegalArgumentException("user with id " + user.getId() + " already exists");
        }

        return userDao.addUser(user);

    }
}
