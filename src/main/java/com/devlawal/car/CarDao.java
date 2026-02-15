package com.devlawal.car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private final List<Car> cars;

    public CarDao() {
        this.cars = new ArrayList<>();
        initializeCars();
    }

    private void initializeCars() {
        // Initialize with sample cars
        cars.add(new Car("1234", Brand.MERCEDES, new BigDecimal("100"), false));
        cars.add(new Car("5678", Brand.TESLA, new BigDecimal("150"), true));
        cars.add(new Car("9012", Brand.TOYOTA, new BigDecimal("120"), false));
        cars.add(new Car("3456", Brand.BUICK, new BigDecimal("110"), false));
        cars.add(new Car("7890", Brand.FORD, new BigDecimal("130"), true));
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars); // Return defensive copy
    }

    public boolean addCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car can't be null");
        }
        return cars.add(car);
    }

    public Car getCarById(String id) {
        return cars.stream()
                .filter(car -> car.getRegNumber().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void deleteCar(String regNumber) {
        if (regNumber == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        cars.removeIf(car -> car.getRegNumber().equals(regNumber));
    }
}
