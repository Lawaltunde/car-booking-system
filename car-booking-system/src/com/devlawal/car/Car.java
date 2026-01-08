package com.devlawal.car;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    private String regNumber;
    private Brand brand;
    private BigDecimal pricePerDay;
    private boolean isElectric;
    private boolean isAvailable;


    public Car(String regNumber, Brand brand, BigDecimal pricePerDay, boolean isElectric) {
        this.regNumber = regNumber;
        this.brand = brand;
        this.pricePerDay = pricePerDay;
        this.isElectric = isElectric;
        this.isAvailable = true;
    }

    public Car(String regNumber, Brand brand, BigDecimal pricePerDay) {
        this.regNumber = regNumber;
        this.brand = brand;
        this.pricePerDay = pricePerDay;
        this.isElectric = false;
        this.isAvailable = true;
    }

    public Car() {
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Car{" +
                "regNumber='" + regNumber + '\'' +
                ", brand=" + brand +
                ", pricePerDay=" + pricePerDay +
                ", isElectric=" + isElectric +
                ", isAvailable=" + isAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric && isAvailable == car.isAvailable && Objects.equals(regNumber, car.regNumber) && brand == car.brand && Objects.equals(pricePerDay, car.pricePerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, brand, pricePerDay, isElectric, isAvailable);
    }
}
