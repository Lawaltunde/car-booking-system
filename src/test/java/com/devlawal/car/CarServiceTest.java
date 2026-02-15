package com.devlawal.car;

import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarService Tests")
class CarServiceTest {

    @Mock
    private CarDao carDao;

    @InjectMocks
    private CarService underTest;

    private List<Car> testCars;
    private Car testCar;

    @BeforeEach
    void setUp() {
        testCars = new ArrayList<>();
        testCars.add(new Car("REG001", Brand.TESLA, new BigDecimal("150"), true));
        testCars.add(new Car("REG002", Brand.TOYOTA, new BigDecimal("100"), false));
        testCars.add(new Car("REG003", Brand.FORD, new BigDecimal("120"), true));
        testCars.add(new Car("REG004", Brand.MERCEDES, new BigDecimal("200"), false));
        
        testCar = new Car("REG999", Brand.BUICK, new BigDecimal("110"), false);
    }

    @Nested
    @DisplayName("getAllCars Tests")
    class GetAllCarsTests {

        @Test
        @DisplayName("Should return all cars from DAO")
        void shouldReturnAllCars() {
            // Given
            when(carDao.getCars()).thenReturn(testCars);

            // When
            List<Car> result = underTest.getAllCars();

            // Then
            assertThat(result)
                    .isNotNull()
                    .hasSize(4)
                    .containsExactlyElementsOf(testCars);
            verify(carDao).getCars();
        }

        @Test
        @DisplayName("Should return empty list when no cars exist")
        void shouldReturnEmptyListWhenNoCars() {
            // Given
            when(carDao.getCars()).thenReturn(new ArrayList<>());

            // When
            List<Car> result = underTest.getAllCars();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAllElectricCars Tests")
    class GetAllElectricCarsTests {

        @Test
        @DisplayName("Should return only electric cars")
        void shouldReturnOnlyElectricCars() {
            // Given
            when(carDao.getCars()).thenReturn(testCars);

            // When
            List<Car> result = underTest.getAllElectricCars();

            // Then
            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .allMatch(Car::isElectric)
                    .extracting(Car::getRegNumber)
                    .containsExactlyInAnyOrder("REG001", "REG003");
        }

        @Test
        @DisplayName("Should return empty list when no electric cars exist")
        void shouldReturnEmptyListWhenNoElectricCars() {
            // Given
            List<Car> nonElectricCars = List.of(
                    new Car("REG005", Brand.TOYOTA, new BigDecimal("100"), false),
                    new Car("REG006", Brand.MERCEDES, new BigDecimal("200"), false)
            );
            when(carDao.getCars()).thenReturn(nonElectricCars);

            // When
            List<Car> result = underTest.getAllElectricCars();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null cars in the list")
        void shouldHandleNullCarsInList() {
            // Given
            List<Car> carsWithNull = new ArrayList<>();
            carsWithNull.add(testCars.get(0));
            carsWithNull.add(null);
            carsWithNull.add(testCars.get(2));
            when(carDao.getCars()).thenReturn(carsWithNull);

            // When
            List<Car> result = underTest.getAllElectricCars();

            // Then
            assertThat(result)
                    .hasSize(2)
                    .doesNotContainNull();
        }
    }

    @Nested
    @DisplayName("getCarById Tests")
    class GetCarByIdTests {

        @Test
        @DisplayName("Should return car when valid ID is provided")
        void shouldReturnCarWhenValidIdProvided() {
            // Given
            String carId = "REG001";
            Car expectedCar = testCars.get(0);
            when(carDao.getCarById(carId)).thenReturn(expectedCar);

            // When
            Car result = underTest.getCarById(carId);

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(expectedCar);
            verify(carDao).getCarById(carId);
        }

        @Test
        @DisplayName("Should throw exception when ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.getCarById(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car registration number cannot be null or empty");
            
            verify(carDao, never()).getCarById(any());
        }

        @Test
        @DisplayName("Should throw exception when ID is blank")
        void shouldThrowExceptionWhenIdIsBlank() {
            // When & Then
            assertThatThrownBy(() -> underTest.getCarById("   "))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car registration number cannot be null or empty");
            
            verify(carDao, never()).getCarById(any());
        }

        @Test
        @DisplayName("Should throw exception when ID is empty")
        void shouldThrowExceptionWhenIdIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> underTest.getCarById(""))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car registration number cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when car is not found")
        void shouldThrowExceptionWhenCarNotFound() {
            // Given
            String nonExistentId = "NONEXISTENT";
            when(carDao.getCarById(nonExistentId)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> underTest.getCarById(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Car with identifier")
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    @DisplayName("addCar Tests")
    class AddCarTests {

        @Test
        @DisplayName("Should successfully add a valid car")
        void shouldSuccessfullyAddValidCar() {
            // Given
            when(carDao.getCars()).thenReturn(new ArrayList<>());
            when(carDao.addCar(testCar)).thenReturn(true);

            // When
            boolean result = underTest.addCar(testCar);

            // Then
            assertThat(result).isTrue();
            verify(carDao).addCar(testCar);
        }

        @Test
        @DisplayName("Should throw exception when car is null")
        void shouldThrowExceptionWhenCarIsNull() {
            // When & Then
            assertThatThrownBy(() -> underTest.addCar(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car cannot be null");
            
            verify(carDao, never()).addCar(any());
        }

        @Test
        @DisplayName("Should throw exception when registration number is null")
        void shouldThrowExceptionWhenRegNumberIsNull() {
            // Given
            Car carWithNullRegNumber = new Car(null, Brand.TOYOTA, new BigDecimal("100"), false);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(carWithNullRegNumber))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car registration number cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when brand is null")
        void shouldThrowExceptionWhenBrandIsNull() {
            // Given
            Car carWithNullBrand = new Car("REG999", null, new BigDecimal("100"), false);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(carWithNullBrand))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car brand cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when price is null")
        void shouldThrowExceptionWhenPriceIsNull() {
            // Given
            Car carWithNullPrice = new Car("REG999", Brand.TOYOTA, null, false);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(carWithNullPrice))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car price cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when price is negative")
        void shouldThrowExceptionWhenPriceIsNegative() {
            // Given
            Car carWithNegativePrice = new Car("REG999", Brand.TOYOTA, new BigDecimal("-10"), false);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(carWithNegativePrice))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car price must be greater than zero");
            
            verify(carDao, never()).addCar(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when price is zero")
        void shouldThrowExceptionWhenPriceIsZero() {
            // Given
            Car carWithZeroPrice = new Car("REG999", Brand.TOYOTA, BigDecimal.ZERO, false);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(carWithZeroPrice))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car price must be greater than zero");
            
            verify(carDao, never()).addCar(any());
        }

        @Test
        @DisplayName("Should throw exception when car with same registration number exists")
        void shouldThrowExceptionWhenDuplicateRegNumberExists() {
            // Given
            Car duplicateCar = new Car("REG001", Brand.BUICK, new BigDecimal("110"), false);
            when(carDao.getCars()).thenReturn(testCars);

            // When & Then
            assertThatThrownBy(() -> underTest.addCar(duplicateCar))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Car with registration number REG001 already exists");
            
            verify(carDao, never()).addCar(any());
        }

        @Test
        @DisplayName("Should successfully add car with same brand but different registration number")
        void shouldAddCarWithSameBrandDifferentRegNumber() {
            // Given
            Car newCar = new Car("REG999", Brand.TESLA, new BigDecimal("160"), true);
            when(carDao.getCars()).thenReturn(testCars);
            when(carDao.addCar(newCar)).thenReturn(true);

            // When
            boolean result = underTest.addCar(newCar);

            // Then
            assertThat(result).isTrue();
            verify(carDao).addCar(newCar);
        }

        @Test
        @DisplayName("Should handle cars with null registration numbers in existing list")
        void shouldHandleCarsWithNullRegNumbersInExistingList() {
            // Given
            List<Car> carsWithNullRegNumber = new ArrayList<>();
            Car carWithNullReg = new Car();
            carWithNullReg.setBrand(Brand.TOYOTA);
            carWithNullReg.setPricePerDay(new BigDecimal("100"));
            carsWithNullRegNumber.add(carWithNullReg);
            
            when(carDao.getCars()).thenReturn(carsWithNullRegNumber);
            when(carDao.addCar(testCar)).thenReturn(true);

            // When
            boolean result = underTest.addCar(testCar);

            // Then
            assertThat(result).isTrue();
        }
    }
}
