package com.devlawal.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CarDao Tests")
class CarDaoTest {

    private CarDao underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarDao();
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize with exactly 5 cars")
        void shouldInitializeWithFiveCars() {
            // When
            List<Car> cars = underTest.getCars();

            // Then
            assertThat(cars).hasSize(5);
        }

        @Test
        @DisplayName("Should initialize with correct car brands")
        void shouldInitializeWithCorrectBrands() {
            // When
            List<Car> cars = underTest.getCars();

            // Then
            assertThat(cars)
                    .extracting(Car::getBrand)
                    .containsExactlyInAnyOrder(
                            Brand.MERCEDES,
                            Brand.TESLA,
                            Brand.TOYOTA,
                            Brand.BUICK,
                            Brand.FORD
                    );
        }

        @Test
        @DisplayName("Should initialize with correct registration numbers")
        void shouldInitializeWithCorrectRegNumbers() {
            // When
            List<Car> cars = underTest.getCars();

            // Then
            assertThat(cars)
                    .extracting(Car::getRegNumber)
                    .containsExactlyInAnyOrder("1234", "5678", "9012", "3456", "7890");
        }

        @Test
        @DisplayName("Should initialize with 2 electric cars")
        void shouldInitializeTwoElectricCars() {
            // When
            List<Car> cars = underTest.getCars();

            // Then
            long electricCount = cars.stream()
                    .filter(Car::isElectric)
                    .count();
            assertThat(electricCount).isEqualTo(2);
        }

        @Test
        @DisplayName("Should initialize electric cars as TESLA and FORD")
        void shouldInitializeCorrectElectricBrands() {
            // When
            List<Car> cars = underTest.getCars();
            List<Brand> electricBrands = cars.stream()
                    .filter(Car::isElectric)
                    .map(Car::getBrand)
                    .toList();

            // Then
            assertThat(electricBrands).containsExactlyInAnyOrder(Brand.TESLA, Brand.FORD);
        }
    }

    @Nested
    @DisplayName("GetCars Tests")
    class GetCarsTests {

        @Test
        @DisplayName("Should return defensive copy of cars list")
        void shouldReturnDefensiveCopy() {
            // When
            List<Car> cars1 = underTest.getCars();
            List<Car> cars2 = underTest.getCars();

            // Then
            assertThat(cars1).isNotSameAs(cars2);
        }

        @Test
        @DisplayName("Should return independent copy - modifications don't affect original")
        void shouldReturnIndependentCopy() {
            // Given
            List<Car> cars = underTest.getCars();
            int originalSize = cars.size();

            // When
            cars.clear();
            List<Car> carsAfterClear = underTest.getCars();

            // Then
            assertThat(carsAfterClear).hasSize(originalSize);
        }

        @Test
        @DisplayName("Should return all 5 initialized cars")
        void shouldReturnAllCars() {
            // When
            List<Car> cars = underTest.getCars();

            // Then
            assertThat(cars).hasSize(5);
        }
    }

    @Nested
    @DisplayName("AddCar Tests")
    class AddCarTests {

        @Test
        @DisplayName("Should successfully add a new car")
        void shouldAddNewCar() {
            // Given
            Car newCar = new Car("9999", Brand.MERCEDES, new BigDecimal("200"), false);
            int initialSize = underTest.getCars().size();

            // When
            boolean result = underTest.addCar(newCar);

            // Then
            assertThat(result).isTrue();
            assertThat(underTest.getCars()).hasSize(initialSize + 1);
        }

        @Test
        @DisplayName("Should contain the newly added car")
        void shouldContainAddedCar() {
            // Given
            Car newCar = new Car("9999", Brand.MERCEDES, new BigDecimal("200"), false);

            // When
            underTest.addCar(newCar);

            // Then
            assertThat(underTest.getCars()).contains(newCar);
        }

        @Test
        @DisplayName("Should add electric car correctly")
        void shouldAddElectricCar() {
            // Given
            Car electricCar = new Car("8888", Brand.TESLA, new BigDecimal("250"), true);

            // When
            underTest.addCar(electricCar);

            // Then
            List<Car> allCars = underTest.getCars();
            assertThat(allCars).contains(electricCar);
            long electricCount = allCars.stream().filter(Car::isElectric).count();
            assertThat(electricCount).isEqualTo(3); // 2 initial + 1 new
        }

        @Test
        @DisplayName("Should throw exception when adding null car")
        void shouldThrowExceptionWhenAddingNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.addCar(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("car can't be null");
        }

        @Test
        @DisplayName("Should allow duplicate registration numbers")
        void shouldAllowDuplicateRegistrationNumbers() {
            // Given
            Car car1 = new Car("DUPLICATE", Brand.TOYOTA, new BigDecimal("100"), false);
            Car car2 = new Car("DUPLICATE", Brand.FORD, new BigDecimal("150"), false);

            // When
            underTest.addCar(car1);
            underTest.addCar(car2);

            // Then
            List<Car> cars = underTest.getCars();
            long duplicateCount = cars.stream()
                    .filter(c -> "DUPLICATE".equals(c.getRegNumber()))
                    .count();
            assertThat(duplicateCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("GetCarById Tests")
    class GetCarByIdTests {

        @Test
        @DisplayName("Should return car when ID exists")
        void shouldReturnCarWhenIdExists() {
            // When
            Car car = underTest.getCarById("1234");

            // Then
            assertThat(car).isNotNull();
            assertThat(car.getRegNumber()).isEqualTo("1234");
            assertThat(car.getBrand()).isEqualTo(Brand.MERCEDES);
        }

        @Test
        @DisplayName("Should return correct car for each initialized registration number")
        void shouldReturnCorrectCarForEachRegNumber() {
            // When & Then
            Car tesla = underTest.getCarById("5678");
            assertThat(tesla.getBrand()).isEqualTo(Brand.TESLA);

            Car toyota = underTest.getCarById("9012");
            assertThat(toyota.getBrand()).isEqualTo(Brand.TOYOTA);

            Car buick = underTest.getCarById("3456");
            assertThat(buick.getBrand()).isEqualTo(Brand.BUICK);

            Car ford = underTest.getCarById("7890");
            assertThat(ford.getBrand()).isEqualTo(Brand.FORD);
        }

        @Test
        @DisplayName("Should return null when ID does not exist")
        void shouldReturnNullWhenIdDoesNotExist() {
            // When
            Car car = underTest.getCarById("NONEXISTENT");

            // Then
            assertThat(car).isNull();
        }

        @Test
        @DisplayName("Should return null when ID is null")
        void shouldReturnNullWhenIdIsNull() {
            // When
            Car car = underTest.getCarById(null);

            // Then
            assertThat(car).isNull();
        }

        @Test
        @DisplayName("Should return first car when multiple cars have same registration")
        void shouldReturnFirstCarWithDuplicateRegistration() {
            // Given
            Car car1 = new Car("DUPLICATE", Brand.TOYOTA, new BigDecimal("100"), false);
            Car car2 = new Car("DUPLICATE", Brand.FORD, new BigDecimal("150"), false);
            underTest.addCar(car1);
            underTest.addCar(car2);

            // When
            Car foundCar = underTest.getCarById("DUPLICATE");

            // Then
            assertThat(foundCar).isNotNull();
            assertThat(foundCar.getBrand()).isEqualTo(Brand.TOYOTA); // First added
        }
    }

    @Nested
    @DisplayName("DeleteCar Tests")
    class DeleteCarTests {

        @Test
        @DisplayName("Should successfully delete existing car")
        void shouldDeleteExistingCar() {
            // Given
            int initialSize = underTest.getCars().size();

            // When
            underTest.deleteCar("1234");

            // Then
            assertThat(underTest.getCars()).hasSize(initialSize - 1);
            assertThat(underTest.getCarById("1234")).isNull();
        }

        @Test
        @DisplayName("Should not throw exception when deleting non-existent car")
        void shouldNotThrowExceptionWhenDeletingNonExistent() {
            // Given
            int initialSize = underTest.getCars().size();

            // When
            underTest.deleteCar("NONEXISTENT");

            // Then
            assertThat(underTest.getCars()).hasSize(initialSize); // Size unchanged
        }

        @Test
        @DisplayName("Should throw exception when registration number is null")
        void shouldThrowExceptionWhenRegNumberIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.deleteCar(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id can't be null");
        }

        @Test
        @DisplayName("Should delete all cars with matching registration number")
        void shouldDeleteAllCarsWithMatchingRegNumber() {
            // Given
            Car car1 = new Car("DUPLICATE", Brand.TOYOTA, new BigDecimal("100"), false);
            Car car2 = new Car("DUPLICATE", Brand.FORD, new BigDecimal("150"), false);
            underTest.addCar(car1);
            underTest.addCar(car2);

            // When
            underTest.deleteCar("DUPLICATE");

            // Then
            List<Car> cars = underTest.getCars();
            long duplicateCount = cars.stream()
                    .filter(c -> "DUPLICATE".equals(c.getRegNumber()))
                    .count();
            assertThat(duplicateCount).isZero();
        }

        @Test
        @DisplayName("Should leave other cars unchanged when deleting one")
        void shouldLeaveOtherCarsUnchanged() {
            // Given
            List<String> otherRegNumbers = List.of("5678", "9012", "3456", "7890");

            // When
            underTest.deleteCar("1234");

            // Then
            List<Car> remainingCars = underTest.getCars();
            assertThat(remainingCars)
                    .extracting(Car::getRegNumber)
                    .containsExactlyInAnyOrderElementsOf(otherRegNumbers);
        }
    }
}
