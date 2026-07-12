package com.julian.booking;

public class CarBookingDao {
    private static final int MAX_SIZE = 5;
    private static int nextIndexOfBooking = 0;
    private static CarBooking[] bookings = new CarBooking[MAX_SIZE];

    public CarBooking[] getBookings() {
        return bookings;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the booking list is full
     */
    public void addBooking(CarBooking booking) {
        if(nextIndexOfBooking >= MAX_SIZE){
            throw new ArrayIndexOutOfBoundsException("Maximum number of bookings reached");
        }
        bookings[nextIndexOfBooking] = booking;
        nextIndexOfBooking++;
    }

    public void deleteBooking(CarBooking booking) {
        for (int i = 0; i < nextIndexOfBooking; i++) {
            if (bookings[i].equals(booking)) {
                for (int j = i; j < nextIndexOfBooking - 1; j++) {
                    bookings[j] = bookings[j + 1];
                }
                bookings[nextIndexOfBooking - 1] = null;
                nextIndexOfBooking--;
                return;
            }
        }
    }
}
