package src.com.devlawal.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class UserService {
    private final UserDao fileUserDao;
    private final UserDao arrayUserDao;

    public UserService(UserDao fileUserDao, UserDao arrayUserDao) {
        this.fileUserDao = fileUserDao;
        this.arrayUserDao = arrayUserDao;
    }

    public List<User> getAllUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(fileUserDao.getUsers());
        all.addAll(arrayUserDao.getUsers());
        return all;
    }

    // returns user corresponding to the given id
    public User getUserById(UUID id) {
        if (id == null) {
            return null;
        }
        return getAllUsers().stream().filter(user -> user.getId() != null && user.getId().equals(id)).findFirst().orElse(null);
    }

    // adds user to database
    public boolean addUser(User user) {
        // user can't be null
        Objects.requireNonNull(user, "user can't be null");
        Objects.requireNonNull(user.getAge(), "user age can't be null");
        if (user.getAge() < 18) {
            throw new IllegalArgumentException("user must be at least 18 years old");
        }
        Objects.requireNonNull(user.getName(), "user name can't be null");
        Objects.requireNonNull(user.getEmail(), "user email can't be null");
        Objects.requireNonNull(user.getId(), "user id can't be null");

        for (User aUser : getAllUsers()) {
            if (aUser != null && aUser.getId() != null && aUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("user with id " + user.getId() + " already exists");
            }
            if (aUser != null && aUser.getEmail() != null && aUser.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("user with email " + user.getEmail() + " already exists");
            }
        }
        return fileUserDao.addUser(user);
    }
}
