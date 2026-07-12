package com.julian.car;

public class CarService {

    private CarDao carDao;

    public CarService() {
        this.carDao = new CarDao();
    }

    public Car[] getCars(){
        return carDao.getCars();
    }

    public Car[] getElectricCars(){
        Car[] cars = getCars();

        int count = 0;
        for (Car car : cars) {
            if (car.isElectric()) {
                count++;
            }
        }

        Car[] electricCars = new Car[count];
        int index = 0;
        for (Car car : cars) {
            if(car.isElectric()){
                electricCars[index] = car;
                index++;
            }
        }

        return electricCars;
    }
}
