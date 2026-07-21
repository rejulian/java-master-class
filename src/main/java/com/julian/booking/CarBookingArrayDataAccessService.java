package com.julian.booking;

import com.julian.user.User;

import java.util.ArrayList;
import java.util.List;

public class CarBookingArrayDataAccessService implements CarBookingDao{
    private static List<CarBooking> bookings = new ArrayList<>();

    @Override
    public List<CarBooking> getBookings() {
        return bookings;
    }

    @Override
    public void addBooking(CarBooking booking) {
        bookings.add(booking);
    }

    @Override
    public void deleteBooking(CarBooking booking) {
        bookings.remove(booking);
    }

    @Override
    public List<CarBooking> getUserBookings(User user) {
        List<CarBooking> bookings = getBookings();

        List<CarBooking> userBookings = new ArrayList<>();
        for (CarBooking booking : bookings) {
            if (booking != null && booking.getUser().getId().equals(user.getId())) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

}
