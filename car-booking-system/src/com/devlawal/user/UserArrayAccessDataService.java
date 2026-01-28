package com.devlawal.user;

import java.util.UUID;

public class UserArrayAccessDataService implements UserDao {
    private static User[] users;

    static {
        // initialize the array with some static data
        users = new User[10];
        users[0] = new User("Nakiyah", "Nakiyah@drive.com", 23);
        users[1] = new User("Ademola", "Ademola@drive.com", 29);
        users[2] = new User("Akande", "Akande@drive.com", 27);
        users[3] = new User("Joshua", "Joshua@drive.com", 28);
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
}
