package com.devlawal.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserArrayAccessDataServiceTest {
    private UserDao underTest;
    private User user;


    @BeforeEach
    void setUp() {
        underTest = new UserArrayAccessDataService();
        user = new User("Ademola", "Ademola@drive.com", 29);
    }

    @Test
    void canAddUsers() {
        boolean actual = underTest.addUser(user);
        assertThat(actual).isTrue();
    }

    @Test
    void cannotAddUsers() {
        boolean actual = underTest.addUser(null);
        assertThat(actual).isFalse();
    }

    @Test
    void canReturnAlUsers() {
        int actual = underTest.getUsers().size();
        assertThat(actual).isEqualTo(3); // 3 users initialized by default
    }

    @Test
    void cannotReturnAlUsers() {
        int actual = underTest.getUsers().size();
        assertThat(actual).isNotEqualTo(2);
    }

    @Test
    void canReturnExpectedUserValues() {
        List<User> users = underTest.getUsers();
        assertThat(users.get(0).getEmail()).isEqualTo("Ademola@drive.com");
        assertThat(users.get(0).getAge()).isEqualTo(29);
        assertThat(users.get(0).getName()).isEqualTo("Ademola");

    }

}