package com.devlawal.user;

import java.util.UUID;
import java.util.Arrays;

public class UserDao {
    private static User[] users;




    static {
        users = new User[10];
        users[0] = new User("James", "jame@drive.com", 25, UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"));
        users[1] = new User("Jamila", "jame@drive.com", 25,UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"));
        users[2] = new User("lawal", "lawal@drive.com", 30,UUID.fromString("2ea85178-fada-4279-9d5e-eea627049fa2"));
        users[3] = new User("kamil", "kamil@drive.com", 45,UUID.fromString("576590ff-57a1-4df3-8430-79980eb42343"));
        users[4] = new User("bob", "bob@drive.com", 65,UUID.fromString("9d818235-ce3b-40e8-b74a-3674985c6bcd"));

        }

    public User[] getUsers() {
        return users;
    }

    public boolean addUser(User user) {

        int index = 0;
        for (User existing : users) {
            if (existing == null){
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

    public void deleteUser(UUID userId){
        int pos = 0;
        for (User user : users) {
            if (user.getId().equals(userId)){
                users[pos] = null;
                break;
            }
            pos++;
        }
        for (int i = pos; i < (users.length - 1); i++) {
            users[i] = users[i+1];
        }
        users[users.length - 1] = null;
    }
}
