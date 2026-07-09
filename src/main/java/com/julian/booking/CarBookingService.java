package com.julian.booking;

import com.julian.car.Car;
import com.julian.car.CarService;
import com.julian.user.User;
import com.julian.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class CarBookingService {
    private CarBookingDao carBookingDao;
    private CarService carService;
    private UserService userService;

    public CarBookingService() {
        this.carBookingDao = new CarBookingDao();
        this.carService = new CarService();
        this.userService = new UserService();

    }

    public CarBooking[] getBookings() {
        return carBookingDao.getBookings();
    }

    public CarBooking[] getUserBookings(User user) {
        CarBooking[] bookings = getBookings();

        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().equals(user)) {
                count++;
            }
        }

        CarBooking[] userBookings = new CarBooking[count];
        int index = 0;
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().equals(user)) {
                userBookings[index] = booking;
                index++;
            }
        }
        return userBookings;
    }

    public void deleteBooking(CarBooking booking){
        carBookingDao.deleteBooking(booking);
    }

    public Car[] getAvailableCars() {
        Car[] cars = carService.getCars();
        CarBooking[] bookings = getBookings();

        if (bookings.length == 0) {
            return cars;
        }

        int count = 0;
        for (Car car : cars) {
            if (!isBooked(car, bookings)) {
                count++;
            }
        }

        Car[] availableCars = new Car[count];
        int index = 0;
        for (Car car : cars) {
            if (!isBooked(car, bookings)) {
                availableCars[index] = car;
                index++;
            }
        }
        return availableCars;
    }

    public void bookCar(UUID userId, Car car, LocalDate startDate, LocalDate endDate){
        try {
            Car[] availableCars = getAvailableCars();

            User user = userService.getUserById(userId);

            for(Car availableCar : availableCars){
                if(availableCar.getId().equals(car.getId())) {
                    CarBooking newBooking = new CarBooking(UUID.randomUUID(), user, car, startDate, endDate, calculatePrice(car, startDate, endDate), BookingStatus.ACTIVE, LocalDateTime.now());
                    carBookingDao.addBooking(newBooking);
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isBooked(Car car, CarBooking[] bookings) {
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
