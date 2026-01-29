package src.com.devlawal.car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;

public class CarService {
    private final CarDao carDao;


    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    // returns all cars
    public List<Car> getAllCars() {
        return carDao.getCars();
    }

    // returns all electric cars
    public List<Car> getAllElectricCars() {
        List<Car> all = getAllCars();
        List<Car> electricCars = new ArrayList<>();
        if (all.isEmpty()) {
            return Collections.emptyList();
        }

        for (Car aCar : all) {
            if (aCar != null && aCar.isElectric()) {
                electricCars.add(aCar);
            }
        }
        if (electricCars.isEmpty()) {
            return Collections.emptyList();
        }
        return electricCars;
    }

    // returns a car corresponding to given id
    public Car getCarById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id can't be null or blank");
        }
        Car theCar = carDao.getCarById(id);
        if (theCar == null) {
            throw new IllegalArgumentException("car with id " + id + " not found");
        }
        return theCar;
    }

    // adds a car to a database
    public boolean addCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car can't be null");
        }

        // validate required fields early to avoid NPEs during duplicate checks
        Objects.requireNonNull(car.getRegNumber(), "car id can't be null");
        Objects.requireNonNull(car.getBrand(), "car brand can't be null");
        Objects.requireNonNull(car.getPricePerDay(), "car price can't be null");
        if (car.getPricePerDay().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("car price can't be negative");
        }

        for (Car aCar : getAllCars()) {
            if (aCar != null && aCar.getRegNumber() != null && aCar.getRegNumber().equals(car.getRegNumber())) {
                throw new IllegalArgumentException("car with id " + car.getRegNumber() + " already exists");
            }
        }
        return carDao.addCar(car);
    }
}
