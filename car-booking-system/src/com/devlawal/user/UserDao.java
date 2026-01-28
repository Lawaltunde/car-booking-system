package com.devlawal.user;

import java.util.UUID;

public interface UserDao {

        User[] getUsers();
        boolean addUser(User user);
        void deleteUser(UUID userId);

    static {
        users = new User[10];
        users[0] = new User("James", "jame@drive.com", 25);
        users[1] = new User("Jamila", "jame@drive.com", 25);
        users[2] = new User("lawal", "lawal@drive.com", 30);
        users[3] = new User("kamil", "kamil@drive.com", 45);
        users[4] = new User("bob", "bob@drive.com", 65);

    }

    public User[] getUsers() {
        return users;
    }

    public boolean addUser(User user) {

        int index = 0;
        for (User existing : users) {
            if (existing == null) {
                users[index] = user;
                return true;
            }
            index++;
        }

        // No null slot found -> array is full. Expand and append the user.
        User[] moreUsers = new User[users.length + 5];
        System.arraycopy(users, 0, moreUsers, 0, users.length);
        moreUsers[users.length] = user;
        users = moreUsers;
        return true;
    }

    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        int pos = 0;
        for (User user : users) {
            if (user.getId().equals(userId)) {
                users[pos] = null;
                break;
            }
            pos++;
        }
        for (int i = pos; i < (users.length - 1); i++) {
            users[i] = users[i + 1];
        }
        users[users.length - 1] = null;
    }
}
