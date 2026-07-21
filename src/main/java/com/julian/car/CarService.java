package com.julian.car;

import java.util.List;

public class CarService {

    private CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getCars(){
        return carDao.getCars();
    }

    public List<Car> getElectricCars(){
        return carDao.getElectricCars();
    }
}
