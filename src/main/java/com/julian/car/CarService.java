package com.julian.car;

public class CarService {

    private CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public Car[] getCars(){
        return carDao.getCars();
    }

    public Car[] getElectricCars(){
        return carDao.getElectricCars();
    }
}
