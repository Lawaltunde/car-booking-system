package com.devlawal.booking;

import com.devlawal.car.Brand;
import com.devlawal.car.Car;
import com.devlawal.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BookingDao Tests")
class BookingDaoTest {

    private BookingDao underTest;
    private User testUser;
    private Car testCar;

    @BeforeEach
    void setUp() {
        underTest = new BookingDao();
        testUser = new User("John Doe", "john@test.com", 30);
        testUser.setId(UUID.randomUUID());
        testCar = new Car("TEST123", Brand.TESLA, new BigDecimal("150"), true);
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize with empty bookings list")
        void shouldInitializeWithEmptyList() {
            // When
            List<Booking> bookings = underTest.getAllBookings();

            // Then
            assertThat(bookings).isEmpty();
        }

        @Test
        @DisplayName("Should return non-null list on initialization")
        void shouldReturnNonNullList() {
            // When
            List<Booking> bookings = underTest.getAllBookings();

            // Then
            assertThat(bookings).isNotNull();
        }
    }

    @Nested
    @DisplayName("AddBooking Tests")
    class AddBookingTests {

        @Test
        @DisplayName("Should successfully add a booking")
        void shouldAddBooking() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );

            // When
            boolean result = underTest.addBooking(booking);

            // Then
            assertThat(result).isTrue();
            assertThat(underTest.getAllBookings()).hasSize(1);
        }

        @Test
        @DisplayName("Should contain the added booking")
        void shouldContainAddedBooking() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );

            // When
            underTest.addBooking(booking);

            // Then
            assertThat(underTest.getAllBookings()).contains(booking);
        }

        @Test
        @DisplayName("Should add multiple bookings successfully")
        void shouldAddMultipleBookings() {
            // Given
            Booking booking1 = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            User user2 = new User("Jane Doe", "jane@test.com", 25);
            user2.setId(UUID.randomUUID());
            Booking booking2 = new Booking(
                    LocalDateTime.now().plusHours(1),
                    new Car("TEST456", Brand.FORD, new BigDecimal("120"), false),
                    user2
            );

            // When
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);

            // Then
            assertThat(underTest.getAllBookings()).hasSize(2);
            assertThat(underTest.getAllBookings()).containsExactly(booking1, booking2);
        }

        @Test
        @DisplayName("Should maintain insertion order")
        void shouldMaintainInsertionOrder() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCar, testUser);
            Booking booking2 = new Booking(LocalDateTime.now().plusHours(1), testCar, testUser);
            Booking booking3 = new Booking(LocalDateTime.now().plusHours(2), testCar, testUser);

            // When
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);
            underTest.addBooking(booking3);

            // Then
            assertThat(underTest.getAllBookings()).containsExactly(booking1, booking2, booking3);
        }

        @Test
        @DisplayName("Should allow bookings with same user")
        void shouldAllowSameUserMultipleBookings() {
            // Given
            Booking booking1 = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            Booking booking2 = new Booking(
                    LocalDateTime.now().plusHours(1),
                    new Car("ANOTHER", Brand.TOYOTA, new BigDecimal("100"), false),
                    testUser // Same user
            );

            // When
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);

            // Then
            assertThat(underTest.getAllBookings()).hasSize(2);
        }

        @Test
        @DisplayName("Should allow bookings with same car")
        void shouldAllowSameCarMultipleBookings() {
            // Given
            User user1 = new User("User1", "user1@test.com", 30);
            user1.setId(UUID.randomUUID());
            User user2 = new User("User2", "user2@test.com", 25);
            user2.setId(UUID.randomUUID());

            Booking booking1 = new Booking(
                    LocalDateTime.now(),
                    testCar, // Same car
                    user1
            );
            Booking booking2 = new Booking(
                    LocalDateTime.now().plusHours(2),
                    testCar, // Same car
                    user2
            );

            // When
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);

            // Then
            assertThat(underTest.getAllBookings()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("GetAllBookings Tests")
    class GetAllBookingsTests {

        @Test
        @DisplayName("Should return defensive copy of bookings list")
        void shouldReturnDefensiveCopy() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            underTest.addBooking(booking);

            // When
            List<Booking> bookings1 = underTest.getAllBookings();
            List<Booking> bookings2 = underTest.getAllBookings();

            // Then
            assertThat(bookings1).isNotSameAs(bookings2);
        }

        @Test
        @DisplayName("Should return independent copy - modifications don't affect original")
        void shouldReturnIndependentCopy() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            underTest.addBooking(booking);

            // When
            List<Booking> bookings = underTest.getAllBookings();
            bookings.clear();
            List<Booking> bookingsAfterClear = underTest.getAllBookings();

            // Then
            assertThat(bookingsAfterClear).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list when no bookings exist")
        void shouldReturnEmptyListWhenNoBookings() {
            // When
            List<Booking> bookings = underTest.getAllBookings();

            // Then
            assertThat(bookings).isEmpty();
        }

        @Test
        @DisplayName("Should return all added bookings")
        void shouldReturnAllBookings() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCar, testUser);
            Booking booking2 = new Booking(LocalDateTime.now().plusHours(1), testCar, testUser);
            Booking booking3 = new Booking(LocalDateTime.now().plusHours(2), testCar, testUser);
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);
            underTest.addBooking(booking3);

            // When
            List<Booking> bookings = underTest.getAllBookings();

            // Then
            assertThat(bookings).hasSize(3);
            assertThat(bookings).containsExactly(booking1, booking2, booking3);
        }
    }

    @Nested
    @DisplayName("DeleteBooking Tests")
    class DeleteBookingTests {

        @Test
        @DisplayName("Should successfully delete existing booking")
        void shouldDeleteExistingBooking() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            underTest.addBooking(booking);
            UUID bookingId = booking.getBookingId();

            // When
            underTest.deleteBooking(bookingId);

            // Then
            assertThat(underTest.getAllBookings()).isEmpty();
        }

        @Test
        @DisplayName("Should not throw exception when deleting non-existent booking")
        void shouldNotThrowExceptionWhenDeletingNonExistent() {
            // Given
            Booking booking = new Booking(
                    LocalDateTime.now(),
                    testCar,
                    testUser
            );
            underTest.addBooking(booking);
            UUID nonExistentId = UUID.randomUUID();

            // When
            underTest.deleteBooking(nonExistentId);

            // Then
            assertThat(underTest.getAllBookings()).hasSize(1); // Unchanged
        }

        @Test
        @DisplayName("Should throw exception when booking ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.deleteBooking(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id can't be null");
        }

        @Test
        @DisplayName("Should delete only the specified booking")
        void shouldDeleteOnlySpecifiedBooking() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCar, testUser);
            Booking booking2 = new Booking(LocalDateTime.now().plusHours(1), testCar, testUser);
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);
            UUID bookingId1 = booking1.getBookingId();
            UUID bookingId2 = booking2.getBookingId();

            // When
            underTest.deleteBooking(bookingId1);

            // Then
            List<Booking> remainingBookings = underTest.getAllBookings();
            assertThat(remainingBookings).hasSize(1);
            assertThat(remainingBookings.get(0).getBookingId()).isEqualTo(bookingId2);
        }

        @Test
        @DisplayName("Should handle deleting from empty list gracefully")
        void shouldHandleDeletingFromEmptyList() {
            // When
            underTest.deleteBooking(UUID.randomUUID());

            // Then
            assertThat(underTest.getAllBookings()).isEmpty();
        }

        @Test
        @DisplayName("Should leave other bookings unchanged when deleting one")
        void shouldLeaveOtherBookingsUnchanged() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCar, testUser);
            Booking booking2 = new Booking(LocalDateTime.now().plusHours(1), testCar, testUser);
            Booking booking3 = new Booking(LocalDateTime.now().plusHours(2), testCar, testUser);
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);
            underTest.addBooking(booking3);
            UUID deleteId = booking1.getBookingId();

            // When
            underTest.deleteBooking(deleteId);

            // Then
            assertThat(underTest.getAllBookings()).containsExactly(booking2, booking3);
        }

        @Test
        @DisplayName("Should successfully delete multiple bookings sequentially")
        void shouldDeleteMultipleBookingsSequentially() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCar, testUser);
            Booking booking2 = new Booking(LocalDateTime.now().plusHours(1), testCar, testUser);
            Booking booking3 = new Booking(LocalDateTime.now().plusHours(2), testCar, testUser);
            underTest.addBooking(booking1);
            underTest.addBooking(booking2);
            underTest.addBooking(booking3);
            UUID id1 = booking1.getBookingId();
            UUID id2 = booking2.getBookingId();
            UUID id3 = booking3.getBookingId();

            // When
            underTest.deleteBooking(id1);
            underTest.deleteBooking(id2);

            // Then
            List<Booking> remainingBookings = underTest.getAllBookings();
            assertThat(remainingBookings).hasSize(1);
            assertThat(remainingBookings.get(0).getBookingId()).isEqualTo(id3);
        }
    }
}
