package com.julian.booking;

import com.julian.user.User;

public class CarBookingArrayDataAccessService implements CarBookingDao{
    private static final int MAX_SIZE = 5;
    private static int nextIndexOfBooking = 0;
    private static CarBooking[] bookings = new CarBooking[MAX_SIZE];

    @Override
    public CarBooking[] getBookings() {
        return bookings;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the booking list is full
     */
    @Override
    public void addBooking(CarBooking booking) {
        if(nextIndexOfBooking >= MAX_SIZE){
            throw new ArrayIndexOutOfBoundsException("Maximum number of bookings reached");
        }
        bookings[nextIndexOfBooking] = booking;
        nextIndexOfBooking++;
    }

    @Override
    public void deleteBooking(CarBooking booking) {
        for (int i = 0; i < nextIndexOfBooking; i++) {
            if (bookings[i].getId().equals(booking.getId())) {
                for (int j = i; j < nextIndexOfBooking - 1; j++) {
                    bookings[j] = bookings[j + 1];
                }
                bookings[nextIndexOfBooking - 1] = null;
                nextIndexOfBooking--;
                return;
            }
        }
    }

    @Override
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

}
