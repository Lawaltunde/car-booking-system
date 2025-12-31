package com.devlawal.car;

import java.lang.constant.Constable;
import java.math.BigDecimal;

public class CarDao {
    private static Car[] cars;

    {
        cars = new Car[5];
        cars[0] = new Car("1234", Brand.MERCEDES, new BigDecimal(100), false);
        cars[1] = new Car("5678", Brand.TESLA, new BigDecimal(150), true);
        cars[2] = new Car("9012", Brand.TOYOTA, new BigDecimal(120), false);
        cars[3] = new Car("3456", Brand.BUICK, new BigDecimal(110), false);
        cars[4] = new Car("7890", Brand.FORD, new BigDecimal(130), true);
    }

    public Car[] getCars() {
        return cars;
    }

    public boolean addCar(Car car){
        if (car == null)
            throw new IllegalArgumentException("car can't be null");
        int index = 0;
        for (Car existing : cars) {
            if (existing == null){
                cars[index] = car;
                return true;
            }
            index++;
        }
        Car[] moreCars = new Car[cars.length + 5];
        System.arraycopy(cars, 0, moreCars, 0, cars.length);
        moreCars[cars.length] = car;
        cars = moreCars;
        return true;
    }

    public Car getCarById(String id){
        for (Car car : cars) {
            if (car.getId().equals(id)) return car;
        }
        return null;
    }
}
