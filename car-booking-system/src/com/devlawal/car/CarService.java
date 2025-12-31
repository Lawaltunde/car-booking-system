package com.devlawal.car;

import java.util.Arrays;
import java.util.Objects;
import java.math.BigDecimal;

public class CarService {
    private final CarDao carDao = new CarDao();

// returns all cars in database
    public Car[] getAllCars() {
        Car[] daoCars = carDao.getCars();
//        if (daoCars == null || daoCars.length == 0)
//            return new Car[0];

        int index = 0;
        for (Car car : daoCars) {
            if (car != null)
                index++;
        }
        Car[] allTheCars = new Car[index];
        int i = 0;
        for (Car daoCar : daoCars) {
            if (daoCar != null)
                allTheCars[i++] = daoCar;
        }
        return allTheCars;
    }

    // returns all electric cars in database
    public Car[] getAllElectricCars()
    {
        Car[] all = getAllCars();
        if (all == null || all.length == 0) return new Car[0];

        int index = 0;
        for (Car aCar : all) {
            if (aCar != null && aCar.isElectric())
                index++;
        }
        if (index > 0) {
            Car[] electricCars = new Car[index];
            index = 0;
            for (Car aCar : all) {
                if (aCar != null && aCar.isElectric())
                    electricCars[index++] = aCar;
            }
            return electricCars;
        }
        return new Car[0];
    }

    // returns a car corresponds to given id
    public Car getCarById(String id){
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id can't be null or blank");
        Car theCar = carDao.getCarById(id);
        if (theCar == null)
            throw new IllegalArgumentException("car with id " + id + " not found");
        return theCar;
    }

    // adds a car to database
    public boolean addCar(Car car){
        if (car == null)
            throw new IllegalArgumentException("car can't be null");

        // validate required fields early to avoid NPEs during duplicate checks
        Objects.requireNonNull(car.getId(), "car id can't be null");
        Objects.requireNonNull(car.getBrand(), "car brand can't be null");
        Objects.requireNonNull(car.getPricePerDay(), "car price can't be null");
        if (car.getPricePerDay().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("car price can't be negative");

        for (Car aCar : getAllCars()) {
            if (aCar != null && aCar.getId() != null && aCar.getId().equals(car.getId()))
                throw new IllegalArgumentException("car with id " + car.getId() + " already exists");
        }

        return carDao.addCar(car);
    }
}
