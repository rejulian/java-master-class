package com.julian.booking;

import com.julian.car.Car;
import com.julian.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CarBookingDao {
    CarBooking[] getBookings();
    void addBooking(CarBooking booking);
    void deleteBooking(CarBooking booking);
    CarBooking[] getUserBookings(User user);
}
