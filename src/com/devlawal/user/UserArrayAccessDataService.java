package src.com.devlawal.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserArrayAccessDataService implements UserDao {
    private static List<User> users;

    static {
        // initialize the array with some static data
        users = new ArrayList<>();
        users.add(new User("Ademola", "Ademola@drive.com", 29));
        users.add(new User("Akande", "Akande@drive.com", 27));
        users.add(new User("Joshua", "Joshua@drive.com", 28));
    };


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
}
