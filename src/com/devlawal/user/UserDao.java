package src.com.devlawal.user;

import java.util.UUID;

public interface UserDao {

    User[] getUsers();

    boolean addUser(User user);

    default void deleteUser(UUID userId, User[] users) {
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
