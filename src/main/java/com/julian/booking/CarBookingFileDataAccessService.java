package com.julian.booking;

import com.julian.user.User;

import java.io.*;
import java.util.Arrays;

public class CarBookingFileDataAccessService implements CarBookingDao, Serializable {

    private static final String FILE_PATH = "src/main/resources/bookings.txt";

    @Override
    public CarBooking[] getBookings() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new CarBooking[0];
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {

            return (CarBooking[]) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading bookings from file", e);
        }
    }

    @Override
    public void addBooking(CarBooking booking) {
        CarBooking[] bookings = getBookings();

        CarBooking[] updated = Arrays.copyOf(bookings, bookings.length + 1);
        updated[bookings.length] = booking;

        writeToFile(updated);
    }

    @Override
    public void deleteBooking(CarBooking booking) {
        CarBooking[] bookings = getBookings();

        int indexToRemove = -1;
        for (int i = 0; i < bookings.length; i++) {
            if (bookings[i].getId().equals(booking.getId())) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1) {
            return;
        }

        CarBooking[] updated = new CarBooking[bookings.length - 1];
        for (int i = 0, j = 0; i < bookings.length; i++) {
            if (i == indexToRemove) continue;
            updated[j++] = bookings[i];
        }

        writeToFile(updated);
    }

    @Override
    public CarBooking[] getUserBookings(User user) {
        CarBooking[] bookings = getBookings();

        int count = 0;
        for (CarBooking booking : bookings) {
            if (booking.getUser().getId().equals(user.getId())) {
                count++;
            }
        }

        CarBooking[] userBookings = new CarBooking[count];
        int index = 0;
        for (CarBooking booking : bookings) {
            if (booking.getUser().getId().equals(user.getId())) {
                userBookings[index++] = booking;
            }
        }
        return userBookings;
    }

    private void writeToFile(CarBooking[] objectToWrite) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)) {

            objectOutputStream.writeObject(objectToWrite);

        } catch (IOException e) {
            throw new RuntimeException("Error writing bookings to file", e);
        }
    }
}
