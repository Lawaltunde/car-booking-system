package com.devlawal.booking;

import com.devlawal.car.Brand;
import com.devlawal.car.Car;
import com.devlawal.car.CarService;
import com.devlawal.exception.BookingException;
import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import com.devlawal.user.User;
import com.devlawal.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService Tests")
class BookingServiceTest {

    @Mock
    private BookingDao bookingDao;

    @Mock
    private CarService carService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService underTest;

    private User testUser;
    private Car testCar;
    private Booking testBooking;
    private List<Booking> testBookings;
    private List<Car> testCars;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User("John Doe", "john@example.com", 25);
        testUser.setAvailable(true);

        // Setup test car
        testCar = new Car("REG001", Brand.TESLA, new BigDecimal("150"), true);
        testCar.setAvailable(true);

        // Setup test booking
        testBooking = new Booking(LocalDateTime.now(), testCar, testUser);

        // Setup test bookings list
        testBookings = new ArrayList<>();

        // Setup test cars list
        testCars = new ArrayList<>();
        testCars.add(new Car("REG001", Brand.TESLA, new BigDecimal("150"), true));
        testCars.add(new Car("REG002", Brand.TOYOTA, new BigDecimal("100"), false));
        testCars.add(new Car("REG003", Brand.FORD, new BigDecimal("120"), true));
    }

    @Nested
    @DisplayName("getAllBookings Tests")
    class GetAllBookingsTests {

        @Test
        @DisplayName("Should return all bookings")
        void shouldReturnAllBookings() {
            // Given
            testBookings.add(testBooking);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Booking> result = underTest.getAllBookings();

            // Then
            assertThat(result)
                    .isNotNull()
                    .hasSize(1)
                    .containsExactly(testBooking);
            verify(bookingDao).getAllBookings();
        }

        @Test
        @DisplayName("Should return empty list when no bookings exist")
        void shouldReturnEmptyListWhenNoBookings() {
            // Given
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When
            List<Booking> result = underTest.getAllBookings();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("bookCar Tests")
    class BookCarTests {

        @Test
        @DisplayName("Should successfully book a car when all conditions are met")
        void shouldSuccessfullyBookCar() {
            // Given
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());
            when(bookingDao.addBooking(testBooking)).thenReturn(true);

            // When
            UUID bookingId = underTest.bookCar(testBooking);

            // Then
            assertThat(bookingId)
                    .isNotNull()
                    .isEqualTo(testBooking.getBookingId());
            assertThat(testUser.isAvailable()).isFalse();
            assertThat(testCar.isAvailable()).isFalse();
            assertThat(testBooking.isBooked()).isTrue();
            verify(bookingDao).addBooking(testBooking);
        }

        @Test
        @DisplayName("Should throw exception when booking is null")
        void shouldThrowExceptionWhenBookingIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Booking cannot be null");

            verify(bookingDao, never()).addBooking(any());
        }

        @Test
        @DisplayName("Should throw exception when car is null")
        void shouldThrowExceptionWhenCarIsNull() {
            // Given
            Booking bookingWithNullCar = new Booking(LocalDateTime.now(), null, testUser);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(bookingWithNullCar))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when user is null")
        void shouldThrowExceptionWhenUserIsNull() {
            // Given
            Booking bookingWithNullUser = new Booking(LocalDateTime.now(), testCar, null);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(bookingWithNullUser))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when booking ID is null")
        void shouldThrowExceptionWhenBookingIdIsNull() {
            // Given
            Booking bookingWithNullId = new Booking(LocalDateTime.now(), testCar, testUser);
            bookingWithNullId.setBookingId(null);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(bookingWithNullId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Booking ID cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when booking time is null")
        void shouldThrowExceptionWhenBookingTimeIsNull() {
            // Given
            Booking bookingWithNullTime = new Booking(null, testCar, testUser);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(bookingWithNullTime))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Booking time cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when user ID is null")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // Given
            User userWithNullId = new User("Jane Doe", "jane@example.com", 30);
            userWithNullId.setId(null);
            Booking bookingWithNullUserId = new Booking(LocalDateTime.now(), testCar, userWithNullId);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(bookingWithNullUserId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User ID cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when car is already booked")
        void shouldThrowExceptionWhenCarAlreadyBooked() {
            // Given
            Booking existingBooking = new Booking(LocalDateTime.now(), testCar, testUser);
            testBookings.add(existingBooking);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            User anotherUser = new User("Jane Doe", "jane@example.com", 28);
            Booking newBooking = new Booking(LocalDateTime.now(), testCar, anotherUser);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(newBooking))
                    .isInstanceOf(BookingException.class)
                    .hasMessageContaining("Car with registration")
                    .hasMessageContaining("is already booked");

            verify(bookingDao, never()).addBooking(any());
        }

        @Test
        @DisplayName("Should throw exception when user is not available")
        void shouldThrowExceptionWhenUserNotAvailable() {
            // Given
            testUser.setAvailable(false);
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(testBooking))
                    .isInstanceOf(BookingException.class)
                    .hasMessageContaining("is not available");

            verify(bookingDao, never()).addBooking(any());
        }

        @Test
        @DisplayName("Should throw exception when booking DAO fails to add booking")
        void shouldThrowExceptionWhenDaoFailsToAddBooking() {
            // Given
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());
            when(bookingDao.addBooking(testBooking)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> underTest.bookCar(testBooking))
                    .isInstanceOf(BookingException.class)
                    .hasMessageContaining("Failed to create booking");
        }

        @Test
        @DisplayName("Should handle bookings with null cars in existing list")
        void shouldHandleBookingsWithNullCarsInExistingList() {
            // Given
            Booking bookingWithNullCar = new Booking(LocalDateTime.now(), null, testUser);
            testBookings.add(bookingWithNullCar);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);
            when(bookingDao.addBooking(testBooking)).thenReturn(true);

            // When
            UUID result = underTest.bookCar(testBooking);

            // Then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("checkBookedUser Tests")
    class CheckBookedUserTests {

        @Test
        @DisplayName("Should return booking when user has a booking")
        void shouldReturnBookingWhenUserHasBooking() {
            // Given
            testBookings.add(testBooking);
            when(userService.getUserById(testUser.getId())).thenReturn(testUser);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            Booking result = underTest.checkBookedUser(testUser.getId());

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(testBooking);
        }

        @Test
        @DisplayName("Should return null when user has no booking")
        void shouldReturnNullWhenUserHasNoBooking() {
            // Given
            when(userService.getUserById(testUser.getId())).thenReturn(testUser);
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When
            Booking result = underTest.checkBookedUser(testUser.getId());

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null when bookings list is empty")
        void shouldReturnNullWhenBookingsListIsEmpty() {
            // Given
            when(userService.getUserById(testUser.getId())).thenReturn(testUser);
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When
            Booking result = underTest.checkBookedUser(testUser.getId());

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should throw exception when user ID is null")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.checkBookedUser(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("User ID cannot be null");

            verify(userService, never()).getUserById(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user is not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            UUID nonExistentUserId = UUID.randomUUID();
            when(userService.getUserById(nonExistentUserId))
                    .thenThrow(new ResourceNotFoundException("User", nonExistentUserId.toString()));

            // When & Then
            assertThatThrownBy(() -> underTest.checkBookedUser(nonExistentUserId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User with identifier")
                    .hasMessageContaining("not found");
        }

        @Test
        @DisplayName("Should handle bookings with null users")
        void shouldHandleBookingsWithNullUsers() {
            // Given
            Booking bookingWithNullUser = new Booking(LocalDateTime.now(), testCar, null);
            testBookings.add(bookingWithNullUser);
            testBookings.add(testBooking);
            when(userService.getUserById(testUser.getId())).thenReturn(testUser);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            Booking result = underTest.checkBookedUser(testUser.getId());

            // Then
            assertThat(result).isEqualTo(testBooking);
        }

        @Test
        @DisplayName("Should handle bookings with users having null IDs")
        void shouldHandleBookingsWithUsersHavingNullIds() {
            // Given
            User userWithNullId = new User("Jane", "jane@example.com", 25);
            userWithNullId.setId(null);
            Booking bookingWithNullUserId = new Booking(LocalDateTime.now(), testCar, userWithNullId);
            testBookings.add(bookingWithNullUserId);
            testBookings.add(testBooking);
            when(userService.getUserById(testUser.getId())).thenReturn(testUser);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            Booking result = underTest.checkBookedUser(testUser.getId());

            // Then
            assertThat(result).isEqualTo(testBooking);
        }
    }

    @Nested
    @DisplayName("getAllAvailableCars Tests")
    class GetAllAvailableCarsTests {

        @Test
        @DisplayName("Should return all cars when no bookings exist")
        void shouldReturnAllCarsWhenNoBookings() {
            // Given
            when(carService.getAllCars()).thenReturn(testCars);
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactlyElementsOf(testCars);
        }

        @Test
        @DisplayName("Should return only unbooked cars")
        void shouldReturnOnlyUnbookedCars() {
            // Given
            Booking booking1 = new Booking(LocalDateTime.now(), testCars.get(0), testUser);
            testBookings.add(booking1);
            when(carService.getAllCars()).thenReturn(testCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result)
                    .hasSize(2)
                    .doesNotContain(testCars.get(0))
                    .extracting(Car::getRegNumber)
                    .containsExactlyInAnyOrder("REG002", "REG003");
        }

        @Test
        @DisplayName("Should return empty list when all cars are booked")
        void shouldReturnEmptyListWhenAllCarsBooked() {
            // Given
            testCars.forEach(car -> {
                Booking booking = new Booking(LocalDateTime.now(), car, testUser);
                testBookings.add(booking);
            });
            when(carService.getAllCars()).thenReturn(testCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty list when no cars exist")
        void shouldReturnEmptyListWhenNoCars() {
            // Given
            when(carService.getAllCars()).thenReturn(new ArrayList<>());

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle bookings with null cars")
        void shouldHandleBookingsWithNullCars() {
            // Given
            Booking bookingWithNullCar = new Booking(LocalDateTime.now(), null, testUser);
            testBookings.add(bookingWithNullCar);
            when(carService.getAllCars()).thenReturn(testCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Should handle bookings with cars having null registration numbers")
        void shouldHandleBookingsWithCarsHavingNullRegNumbers() {
            // Given
            Car carWithNullReg = new Car();
            carWithNullReg.setBrand(Brand.TOYOTA);
            Booking bookingWithNullRegCar = new Booking(LocalDateTime.now(), carWithNullReg, testUser);
            testBookings.add(bookingWithNullRegCar);
            when(carService.getAllCars()).thenReturn(testCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableCars();

            // Then
            assertThat(result).hasSize(3);
        }
    }

    @Nested
    @DisplayName("getAllAvailableElectricCars Tests")
    class GetAllAvailableElectricCarsTests {

        @Test
        @DisplayName("Should return all electric cars when no bookings exist")
        void shouldReturnAllElectricCarsWhenNoBookings() {
            // Given
            List<Car> electricCars = testCars.stream()
                    .filter(Car::isElectric)
                    .toList();
            when(carService.getAllElectricCars()).thenReturn(electricCars);
            when(bookingDao.getAllBookings()).thenReturn(new ArrayList<>());

            // When
            List<Car> result = underTest.getAllAvailableElectricCars();

            // Then
            assertThat(result)
                    .hasSize(2)
                    .allMatch(Car::isElectric)
                    .extracting(Car::getRegNumber)
                    .containsExactlyInAnyOrder("REG001", "REG003");
        }

        @Test
        @DisplayName("Should return only unbooked electric cars")
        void shouldReturnOnlyUnbookedElectricCars() {
            // Given
            List<Car> electricCars = testCars.stream()
                    .filter(Car::isElectric)
                    .toList();
            Booking booking1 = new Booking(LocalDateTime.now(), testCars.get(0), testUser);
            testBookings.add(booking1);
            when(carService.getAllElectricCars()).thenReturn(electricCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableElectricCars();

            // Then
            assertThat(result)
                    .hasSize(1)
                    .extracting(Car::getRegNumber)
                    .containsExactly("REG003");
        }

        @Test
        @DisplayName("Should return empty list when all electric cars are booked")
        void shouldReturnEmptyListWhenAllElectricCarsBooked() {
            // Given
            List<Car> electricCars = testCars.stream()
                    .filter(Car::isElectric)
                    .toList();
            electricCars.forEach(car -> {
                Booking booking = new Booking(LocalDateTime.now(), car, testUser);
                testBookings.add(booking);
            });
            when(carService.getAllElectricCars()).thenReturn(electricCars);
            when(bookingDao.getAllBookings()).thenReturn(testBookings);

            // When
            List<Car> result = underTest.getAllAvailableElectricCars();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty list when no electric cars exist")
        void shouldReturnEmptyListWhenNoElectricCars() {
            // Given
            when(carService.getAllElectricCars()).thenReturn(new ArrayList<>());

            // When
            List<Car> result = underTest.getAllAvailableElectricCars();

            // Then
            assertThat(result).isEmpty();
        }
    }
}
