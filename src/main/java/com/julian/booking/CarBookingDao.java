package com.julian.booking;

import com.julian.user.User;

import java.util.List;

public interface CarBookingDao {
    List<CarBooking> getBookings();
    void addBooking(CarBooking booking);
    void deleteBooking(CarBooking booking);
    List<CarBooking> getUserBookings(User user);
}
