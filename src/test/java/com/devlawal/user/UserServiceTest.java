package com.devlawal.user;

import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {
    @Mock
    private UserDao userDao;

    private UserService underTest;

    private List<User> users;
    private User testUser;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>(List.of(
            new User("James", "james@drive.com", 25),
            new User("Jamila", "jamila@drive.com", 26),
            new User("Lawal", "lawal@drive.com", 30),
            new User("Kamil", "kamil@drive.com", 45)
        ));
        testUser = new User("John", "john@drive.com", 28);
        underTest = new UserService(userDao);
    }

    @Nested
    @DisplayName("getAllUsers Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users from DAO")
        void shouldReturnAllUsers() {
            // Given
            when(userDao.getUsers()).thenReturn(users);

            // When
            List<User> result = underTest.getAllUsers();

            // Then
            assertThat(result)
                    .isNotNull()
                    .hasSize(4)
                    .containsExactlyElementsOf(users);
            verify(userDao).getUsers();
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsers() {
            // Given
            when(userDao.getUsers()).thenReturn(new ArrayList<>());

            // When
            List<User> result = underTest.getAllUsers();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getUserById Tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when valid ID is provided")
        void shouldReturnUserWhenValidIdProvided() {
            // Given
            when(userDao.getUsers()).thenReturn(users);
            UUID userId = users.get(0).getId();

            // When
            User result = underTest.getUserById(userId);

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(users.get(0));
        }

        @Test
        @DisplayName("Should throw ValidationException when user ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // When / Then
            assertThatThrownBy(() -> underTest.getUserById(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User ID cannot be null");
            verify(userDao, never()).getUsers();
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user is not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userDao.getUsers()).thenReturn(users);
            UUID nonExistentId = UUID.randomUUID();

            // When / Then
            assertThatThrownBy(() -> underTest.getUserById(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User with identifier")
                    .hasMessageContaining("not found");
        }

        @Test
        @DisplayName("Should handle users with null IDs in the list")
        void shouldHandleUsersWithNullIds() {
            // Given
            User userWithNullId = new User("Test", "test@drive.com", 25);
            userWithNullId.setId(null);
            users.add(userWithNullId);
            when(userDao.getUsers()).thenReturn(users);
            UUID searchId = users.get(0).getId();

            // When
            User result = underTest.getUserById(searchId);

            // Then
            assertThat(result).isEqualTo(users.get(0));
        }
    }

    @Nested
    @DisplayName("addUser Tests")
    class AddUserTests {

        @Test
        @DisplayName("Should successfully add a valid user")
        void shouldSuccessfullyAddValidUser() {
            // Given
            when(userDao.getUsers()).thenReturn(new ArrayList<>());
            when(userDao.addUser(testUser)).thenReturn(true);

            // When
            boolean result = underTest.addUser(testUser);

            // Then
            assertThat(result).isTrue();
            verify(userDao).addUser(testUser);
        }

        @Test
        @DisplayName("Should throw ValidationException when user is null")
        void shouldThrowExceptionWhenUserIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.addUser(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User cannot be null");

            verify(userDao, never()).addUser(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when user age defaults to 0 (less than 18)")
        void shouldThrowExceptionWhenAgeIsZero() {
            // Given
            User userWithDefaultAge = new User();
            userWithDefaultAge.setName("Test");
            userWithDefaultAge.setEmail("test@drive.com");
            userWithDefaultAge.setId(UUID.randomUUID());
            // age defaults to 0 with primitive int

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(userWithDefaultAge))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Age must be at least 18 years old");
        }

        @Test
        @DisplayName("Should throw ValidationException when all required fields are null")
        void shouldThrowExceptionWhenAllFieldsNull() {
            // Given
            User userWithAllNullFields = new User();

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(userWithAllNullFields))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw ValidationException when user ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // Given
            User userWithNullId = new User("Test", "test@drive.com", 25);
            userWithNullId.setId(null);

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(userWithNullId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User ID cannot be null");
        }

        @Test
        @DisplayName("Should throw ValidationException when user age is less than 18")
        void shouldThrowExceptionWhenAgeLessThan18() {
            // Given
            User underageUser = new User("Minor", "minor@drive.com", 17);

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(underageUser))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Age must be at least 18 years old");

            verify(userDao, never()).addUser(any());
        }

        @Test
        @DisplayName("Should successfully add user with age exactly 18")
        void shouldAddUserWithAgeExactly18() {
            // Given
            User userAge18 = new User("Young Adult", "young@drive.com", 18);
            when(userDao.getUsers()).thenReturn(new ArrayList<>());
            when(userDao.addUser(userAge18)).thenReturn(true);

            // When
            boolean result = underTest.addUser(userAge18);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should throw ValidationException when user with same ID exists")
        void shouldThrowExceptionWhenDuplicateIdExists() {
            // Given
            User existingUser = users.get(0);
            User duplicateUser = new User("Duplicate", "duplicate@drive.com", 25);
            duplicateUser.setId(existingUser.getId());
            when(userDao.getUsers()).thenReturn(users);

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(duplicateUser))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User with ID")
                    .hasMessageContaining("already exists");

            verify(userDao, never()).addUser(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when user with same email exists")
        void shouldThrowExceptionWhenDuplicateEmailExists() {
            // Given
            String existingEmail = users.get(0).getEmail();
            User duplicateEmailUser = new User("Duplicate Name", existingEmail, 30);
            when(userDao.getUsers()).thenReturn(users);

            // When & Then
            assertThatThrownBy(() -> underTest.addUser(duplicateEmailUser))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User with email")
                    .hasMessageContaining("already exists");

            verify(userDao, never()).addUser(any());
        }

        @Test
        @DisplayName("Should successfully add user with unique ID and email")
        void shouldAddUserWithUniqueIdAndEmail() {
            // Given
            when(userDao.getUsers()).thenReturn(users);
            when(userDao.addUser(testUser)).thenReturn(true);

            // When
            boolean result = underTest.addUser(testUser);

            // Then
            assertThat(result).isTrue();
            verify(userDao).addUser(testUser);
        }

        @Test
        @DisplayName("Should handle users with null emails in existing list")
        void shouldHandleUsersWithNullEmailsInExistingList() {
            // Given
            User userWithNullEmail = new User();
            userWithNullEmail.setName("Test");
            userWithNullEmail.setId(UUID.randomUUID());
            users.add(userWithNullEmail);
            when(userDao.getUsers()).thenReturn(users);
            when(userDao.addUser(testUser)).thenReturn(true);

            // When
            boolean result = underTest.addUser(testUser);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should handle users with null IDs in existing list")
        void shouldHandleUsersWithNullIdsInExistingList() {
            // Given
            User userWithNullId = new User("Test", "test@drive.com", 25);
            userWithNullId.setId(null);
            users.add(userWithNullId);
            when(userDao.getUsers()).thenReturn(users);
            when(userDao.addUser(testUser)).thenReturn(true);

            // When
            boolean result = underTest.addUser(testUser);

            // Then
            assertThat(result).isTrue();
        }
    }
}
