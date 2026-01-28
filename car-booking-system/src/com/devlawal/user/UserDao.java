package com.devlawal.user;

public interface UserDao {
        User[] getUsers();
        boolean addUser(User user);
        void deleteUser(java.util.UUID userId);
}
