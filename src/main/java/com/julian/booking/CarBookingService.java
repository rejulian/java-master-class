package com.julian.booking;

import com.julian.car.Car;
import com.julian.car.CarService;
import com.julian.user.User;
import com.julian.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingService {
    private CarBookingDao carBookingDao;
    private CarService carService;
    private UserService userService;

    public CarBookingService(CarBookingDao carBookingDao, CarService carService, UserService userService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
        this.userService = userService;
    }

    public List<CarBooking> getBookings() {
        return carBookingDao.getBookings();
    }

    public List<CarBooking> getUserBookings(User user) {
        return carBookingDao.getUserBookings(user);
    }

    public void deleteBooking(CarBooking booking){
        carBookingDao.deleteBooking(booking);
    }

    public List<Car> getAvailableCars() {
        List<Car> cars = carService.getCars();
        List<CarBooking> bookings = getBookings();

        if (bookings.isEmpty()) {
            return cars;
        }

        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (!isBooked(car, bookings)) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public void bookCar(UUID userId, Car car, LocalDate startDate, LocalDate endDate){
        List<Car> availableCars = getAvailableCars();

        User user = userService.getUserById(userId);

        for(Car availableCar : availableCars){
            if(availableCar.getId().equals(car.getId())) {
                CarBooking newBooking = new CarBooking(UUID.randomUUID(), user, car, startDate, endDate, calculatePrice(car, startDate, endDate), BookingStatus.ACTIVE, LocalDateTime.now());
                carBookingDao.addBooking(newBooking);
                break;
            }
        }
    }

    private boolean isBooked(Car car, List<CarBooking> bookings) {
        for (CarBooking booking : bookings) {
            if (booking == null) continue;
            if (car.getId().equals(booking.getCar().getId()) && booking.getStatus().equals(BookingStatus.ACTIVE)) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal calculatePrice(Car car, LocalDate startDate, LocalDate endDate){
        long days = ChronoUnit.DAYS.between(startDate, endDate);

        if(days == 0){
            return car.getRentalPricePerDay();
        }

        return car.getRentalPricePerDay().multiply(new BigDecimal(days));
    }
}
