package com.devlawal.user;

import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import com.devlawal.util.ValidationUtil;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getUsers();
    }

    public User getUserById(UUID id) {
        if (id == null) {
            throw new ValidationException("User ID cannot be null");
        }
        return getAllUsers().stream()
                .filter(user -> user.getId() != null && user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }

    public boolean addUser(User user) {
        // Validate user object
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }
        
        ValidationUtil.validateNotEmpty(user.getName(), "User name");
        ValidationUtil.validateNotEmpty(user.getEmail(), "User email");
        
        if (user.getId() == null) {
            throw new ValidationException("User ID cannot be null");
        }

        ValidationUtil.validateEmail(user.getEmail());
        
        ValidationUtil.validateAge(user.getAge());

        boolean idExists = getAllUsers().stream()
                .anyMatch(existingUser -> existingUser.getId() != null 
                        && existingUser.getId().equals(user.getId()));
        
        if (idExists) {
            throw new ValidationException("User with ID " + user.getId() + " already exists");
        }

        boolean emailExists = getAllUsers().stream()
                .anyMatch(existingUser -> existingUser.getEmail() != null 
                        && existingUser.getEmail().equalsIgnoreCase(user.getEmail().trim()));
        
        if (emailExists) {
            throw new ValidationException("User with email " + user.getEmail() + " already exists");
        }

        return userDao.addUser(user);
    }
}
