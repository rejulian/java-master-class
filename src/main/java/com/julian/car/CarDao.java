package com.julian.car;

import java.util.List;

public interface CarDao {
    List<Car> getCars();
    List<Car> getElectricCars();
}
