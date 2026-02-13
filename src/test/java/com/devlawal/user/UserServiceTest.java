package com.devlawal.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao fileUserDao;
    @Mock
    private UserDao arrayUserDao;
    @Mock
    private UserDao fakeUserDao;

    @InjectMocks
    private UserService underTest;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>(List.of(
            new User("James", "jame@drive.com", 25),
            new User("Jamila", "jamila@drive.com", 25),
            new User("lawal", "lawal@drive.com", 30),
            new User("kamil", "kamil@drive.com", 45)
        ));
        underTest = new UserService(fileUserDao, arrayUserDao, fakeUserDao);
    }

    @Test
    void canGetActualSizeOfAvailableUsers() {
        when(fileUserDao.getUsers()).thenReturn(users);
        when(arrayUserDao.getUsers()).thenReturn(users);
        when(fakeUserDao.getUsers()).thenReturn(users);
        int actual = underTest.getAllUsers().size();
        assertThat(actual).isEqualTo(12);
    }

    @Test
    void canGetUserById() {
        when(fileUserDao.getUsers()).thenReturn(users);
        when(arrayUserDao.getUsers()).thenReturn(users);
        when(fakeUserDao.getUsers()).thenReturn(users);
        underTest.getAllUsers().forEach(user -> {
            assertThat(user.getId()).isNotNull();
            assertThat(user.getEmail()).isNotNull();
            assertThat(user.getId()).isNotNull();
            assertThat(user.getAge()).isGreaterThan(0);
        });
    }

    @Test
    void canAddUser() {
        User user = new User("Jojo", "jojo@drive.com", 29);
        boolean actual = underTest.addUser(user);
        assertThat(actual).isTrue();
    }

    @Test
    void willThrowExceptionIfUserIsNull() {
        assertThatThrownBy(() -> underTest.addUser(null)).isInstanceOf(NullPointerException.class).
                hasMessageContaining("user can't be null");
    }
}