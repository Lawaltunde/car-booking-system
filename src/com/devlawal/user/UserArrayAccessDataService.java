package src.com.devlawal.user;

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
}
