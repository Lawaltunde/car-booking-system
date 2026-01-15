package com.devlawal.user;

import java.util.UUID;

public interface UserDao {

        User[] getUsers();
        boolean addUser(User user);
        void deleteUser(UUID userId);

}
