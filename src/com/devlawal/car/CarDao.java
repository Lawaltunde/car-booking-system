package src.com.devlawal.car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private static List<Car> cars;

    {
        cars = new ArrayList<>();
        cars.add(new Car("1234", Brand.MERCEDES, new BigDecimal(100), false));
        cars.add(new Car("5678", Brand.TESLA, new BigDecimal(150), true));
        cars.add(new Car("9012", Brand.TOYOTA, new BigDecimal(120), false));
        cars.add(new Car("3456", Brand.BUICK, new BigDecimal(110), false));
        cars.add(new Car("7890", Brand.FORD, new BigDecimal(130), true));
    }

    public List<Car> getCars() {
        return cars;
    }

    public boolean addCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car can't be null");
        }
        // I will further improve this later to return id instead of boolean
        return  cars.add(car);
    }

    public Car getCarById(String id) {
        for (Car car : cars) {
            if (car.getRegNumber().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public void deleteCar(String regNumber) {
        if (regNumber == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        for (Car car : cars) {
            if (car.getRegNumber().equals(regNumber)) {
                cars.remove(car);
            }
        }
    }
}
