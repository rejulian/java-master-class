package com.julian.booking;

import com.julian.user.User;

public interface CarBookingDao {
    CarBooking[] getBookings();
    void addBooking(CarBooking booking);
    void deleteBooking(CarBooking booking);
    CarBooking[] getUserBookings(User user);
}
