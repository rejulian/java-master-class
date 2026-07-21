package com.julian.car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {
    private static final List<Car> CARS = new ArrayList<>();

    static {
        CARS.add(new Car(UUID.fromString("e10d126a-3608-4980-9f9c-aa179f5cebc3"), "Mercedes C200", new BigDecimal("80.00"), Brand.MERCEDES, false));
        CARS.add(new Car(UUID.fromString("2d2b9d2b-aaaf-4bf2-834a-e02964e10fc3"), "Tesla Model 3", new BigDecimal("100.00"), Brand.TESLA, true));
        CARS.add(new Car(UUID.fromString("a3c51d2b-aaaf-4bf2-834a-e02964e10fc3"), "Audi A1", new BigDecimal("50.00"), Brand.AUDI, false));
    }

    @Override
    public List<Car> getCars() {
        return CARS;
    }

    @Override
    public List<Car> getElectricCars() {
        List<Car> cars = getCars();

        List<Car> electricCars = new ArrayList<>();
        for (Car car : cars) {
            if(car.isElectric()){
                electricCars.add(car);
            }
        }

        return electricCars;
    }
}
