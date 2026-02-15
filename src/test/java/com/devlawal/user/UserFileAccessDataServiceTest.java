package com.devlawal.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class UserFileAccessDataServiceTest {

    private UserFileAccessDataService underTest;
    private User user;

    @BeforeEach
    void setUp() {
        underTest = new UserFileAccessDataService();
        user = new User("Ademola", "Ademola@drive.com", 29);
    }

    @Test
    void canGetUser() {
        int actual = underTest.getUsers().size();
        assertThat(actual).isEqualTo(9);
    }

    @Test
    void canAddUser() {
        boolean actual = underTest.addUser(user);
        assertThat(actual).isTrue();
        assertThat(underTest.getUsers().size()).isEqualTo(10);
    }
}