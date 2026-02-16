package com.devlawal.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserFakerDataAccessServiceTest {
    private UserFakerDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserFakerDataAccessService();
    }

    @Test
    void canGetUsers() {
        int actual = underTest.getUsers().size();
        assertThat(actual).isEqualTo(20);
    }

    @Test
    void canAddUser() {
        User user = new User("Anjenna", "Anjenna@gmail.com", 40);
        boolean actual = underTest.addUser(user);
        assertThat(actual).isTrue();
    }

    @Test
    void dataFromFakerAreNotNull() {
        User user = new User("Anjenna", "Anjenna@gmail.com", 40);
        underTest.addUser(user);
        User actual = underTest.getUsers().get(20);
        assertThat(actual.getName()).isEqualTo("Anjenna");
        assertThat(actual.getAge()).isEqualTo(40);
        assertThat(actual.getEmail()).isEqualTo("Anjenna@gmail.com");
    }
}