package com.julian.booking;

import com.julian.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarBookingFileDataAccessService implements CarBookingDao, Serializable {

    private static final String FILE_PATH = "src/main/resources/bookings.txt";

    @Override
    public List<CarBooking> getBookings() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {

            return (List<CarBooking>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading bookings from file", e);
        }
    }

    @Override
    public void addBooking(CarBooking booking) {
        List<CarBooking> bookings = getBookings();
        bookings.add(booking);
        writeToFile(bookings);
    }

    @Override
    public void deleteBooking(CarBooking booking) {
        List<CarBooking> bookings = getBookings();
        bookings.remove(booking);
        writeToFile(bookings);
    }

    @Override
    public List<CarBooking> getUserBookings(User user) {
        List<CarBooking> bookings = getBookings();
        List<CarBooking> userBookings = new ArrayList<>();

        for (CarBooking booking : bookings) {
            if (booking.getUser().getId().equals(user.getId())) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    private void writeToFile(List<CarBooking> listToWrite) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)) {

            objectOutputStream.writeObject(listToWrite);

        } catch (IOException e) {
            throw new RuntimeException("Error writing bookings to file", e);
        }
    }
}
