package src.com.devlawal.user;

import java.util.List;
import java.util.UUID;

public interface UserDao {

    List<User> getUsers();

    boolean addUser(User user);

    default void deleteUser(UUID userId, List<User> users) {
        if (userId == null) {
            throw new IllegalArgumentException("id can't be null");
        }

        if (users == null || users.isEmpty()) {
            // nothing to delete
            return;
        }
        users.removeIf(user -> user.isAvailable() && user.getId().equals(userId));
    }

}
