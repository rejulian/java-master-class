package com.julian.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarDao {
    private static final Car[] CARS;

    static {
        CARS = new Car[]{
                new Car(UUID.fromString("e10d126a-3608-4980-9f9c-aa179f5cebc3"), "Mercedes C200", new BigDecimal("80.00"), Brand.MERCEDES, false),
                new Car(UUID.fromString("2d2b9d2b-aaaf-4bf2-834a-e02964e10fc3"), "Tesla Model 3", new BigDecimal("100.00"), Brand.TESLA, true),
                new Car(UUID.fromString("a3c51d2b-aaaf-4bf2-834a-e02964e10fc3"), "Audi A1", new BigDecimal("50.00"), Brand.AUDI, false)
        };
    }

    public Car[] getCars() {
        return CARS;
    }
}
