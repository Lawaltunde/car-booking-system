package com.devlawal.car;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    private String regNumber;
    private Brand brand;
    private BigDecimal pricePerDay;
    private boolean isElectric;


    public Car(String regNumber, Brand brand, BigDecimal pricePerDay, boolean isElectric) {
        this.regNumber = regNumber;
        this.brand = brand;
        this.pricePerDay = pricePerDay;
        this.isElectric = isElectric;
    }

    public Car(String regNumber, Brand brand, BigDecimal pricePerDay) {
        this.regNumber = regNumber;
        this.brand = brand;
        this.pricePerDay = pricePerDay;
        this.isElectric = false;
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

    @Override
    public String toString() {
        return "Car{" +
                "id='" + regNumber + '\'' +
                ", brand=" + brand +
                ", pricePerDay=" + pricePerDay +
                ", isElectric=" + isElectric +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return isElectric == car.isElectric && Objects.equals(regNumber, car.regNumber) && brand == car.brand && Objects.equals(pricePerDay, car.pricePerDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, brand, pricePerDay, isElectric);
    }
}
