package com.devlawal.car;

import com.devlawal.exception.ResourceNotFoundException;
import com.devlawal.exception.ValidationException;
import com.devlawal.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

public class CarService {
    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getAllCars() {
        return carDao.getCars();
    }

    public List<Car> getAllElectricCars() {
        return getAllCars().stream()
                .filter(car -> car != null && car.isElectric())
                .toList();
    }

    public Car getCarById(String id) {
        ValidationUtil.validateNotEmpty(id, "Car registration number");
        
        Car theCar = carDao.getCarById(id);
        if (theCar == null) {
            throw new ResourceNotFoundException("Car", id);
        }
        return theCar;
    }

    public boolean addCar(Car car) {
        if (car == null) {
            throw new ValidationException("Car cannot be null");
        }

        ValidationUtil.validateNotEmpty(car.getRegNumber(), "Car registration number");
        
        if (car.getBrand() == null) {
            throw new ValidationException("Car brand cannot be null");
        }
        
        if (car.getPricePerDay() == null) {
            throw new ValidationException("Car price cannot be null");
        }
        
        if (car.getPricePerDay().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Car price must be greater than zero");
        }

        boolean regNumberExists = getAllCars().stream()
                .anyMatch(existingCar -> existingCar.getRegNumber() != null 
                        && existingCar.getRegNumber().equals(car.getRegNumber()));
        
        if (regNumberExists) {
            throw new ValidationException("Car with registration number " + car.getRegNumber() + " already exists");
        }

        return carDao.addCar(car);
    }
}
