package com.devlawal.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThat(underTest.getUsers().size()).isEqualTo(4);
    }

    @Test
    void cannotReturnAlUsers() {
        int actual = underTest.getUsers().size();
        assertThat(actual).isNotEqualTo(2);
    }

}