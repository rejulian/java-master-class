package com.julian;

import com.julian.booking.*;
import com.julian.car.Car;
import com.julian.car.CarArrayDataAccessService;
import com.julian.car.CarDao;
import com.julian.car.CarService;
import com.julian.user.User;
import com.julian.user.UserArrayDataAccessService;
import com.julian.user.UserDao;
import com.julian.user.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final UserDao userArrayDataAccessService = new UserArrayDataAccessService();
    private static final CarDao carArrayDataAccessService = new CarArrayDataAccessService();
    private static final CarBookingDao carBookingArrayDataAccessService = new CarBookingArrayDataAccessService();
//    private static final CarBookingDao carBookingFileDataAccessService = new CarBookingFileDataAccessService();

    private static final UserService USER_SERVICE = new UserService(userArrayDataAccessService);
    private static final CarService CAR_SERVICE = new CarService(carArrayDataAccessService);
    private static final CarBookingService BOOKING_SERVICE = new CarBookingService(carBookingArrayDataAccessService, CAR_SERVICE, USER_SERVICE);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice(1, 8);
            switch (choice) {
                case 1 -> bookCar();
                case 2 -> deleteBooking();
                case 3 -> viewUserBookings();
                case 4 -> viewAllBookings();
                case 5 -> viewAvailableCars();
                case 6 -> viewElectricCars();
                case 7 -> viewAllUsers();
                case 8 -> running = false;
            }
        }
        System.out.println("Goodbye.");
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Car Booking System ===");
        System.out.println("1) Book a Car");
        System.out.println("2) Delete Booking");
        System.out.println("3) View User Bookings");
        System.out.println("4) View All Bookings");
        System.out.println("5) View Available Cars");
        System.out.println("6) View Electric Cars");
        System.out.println("7) View All Users");
        System.out.println("8) Exit");
    }

    private static void bookCar() {
        System.out.println();
        System.out.println("-- Book a Car --");

        User user = pickUser();
        if (user == null) return;

        List<Car> available = BOOKING_SERVICE.getAvailableCars();
        if (available.isEmpty()) {
            System.out.println("No cars available to book.");
            return;
        }

        System.out.println("Available cars:");
        for (int i = 0; i < available.size(); i++) {
            printCar(available.get(i), i + 1);
        }
        int carChoice = readIntInRange("Pick a car: ", 1, available.size());
        Car car = available.get(carChoice - 1);

        LocalDate start = readDate("Start date (YYYY-MM-DD): ");
        LocalDate end = readDate("End date   (YYYY-MM-DD): ");
        if (end.isBefore(start)) {
            System.out.println("End date cannot be before start date. Booking cancelled.");
            return;
        }

        int before = nonNull(BOOKING_SERVICE.getBookings()).size();
        BOOKING_SERVICE.bookCar(user.getId(), car, start, end);
        List<CarBooking> after = nonNull(BOOKING_SERVICE.getBookings());

        if (after.size() == before) {
            System.out.println("Booking failed (car became unavailable or storage full).");
            return;
        }

        CarBooking created = after.get(after.size() - 1);
        System.out.println("Booking confirmed:");
        printBooking(created, 1);
    }

    private static void deleteBooking() {
        System.out.println();
        System.out.println("-- Delete Booking --");

        List<CarBooking> bookings = nonNull(BOOKING_SERVICE.getBookings());
        if (bookings.size() == 0) {
            System.out.println("No bookings to delete.");
            return;
        }

        System.out.println("Bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            printBooking(bookings.get(i), i + 1);
        }
        int choice = readIntInRange("Pick a booking to delete: ", 1, bookings.size());
        CarBooking target = bookings.get(choice - 1);

        BOOKING_SERVICE.deleteBooking(target);
        System.out.println("Deleted booking " + target.getId() + ".");
    }

    private static void viewUserBookings() {
        System.out.println();
        System.out.println("-- View User Bookings --");

        User user = pickUser();
        if (user == null) return;

        List<CarBooking> bookings = BOOKING_SERVICE.getUserBookings(user);
        if (bookings.size() == 0) {
            System.out.println(user.getName() + " has no bookings.");
            return;
        }
        System.out.println("Bookings for " + user.getName() + ":");
        for (int i = 0; i < bookings.size(); i++) {
            printBooking(bookings.get(i), i + 1);
        }
    }

    private static void viewAllBookings() {
        System.out.println();
        System.out.println("-- All Bookings --");

        List<CarBooking> bookings = nonNull(BOOKING_SERVICE.getBookings());
        if (bookings.size() == 0) {
            System.out.println("No bookings in the system.");
            return;
        }
        for (int i = 0; i < bookings.size(); i++) {
            printBooking(bookings.get(i), i + 1);
        }
    }

    private static void viewAvailableCars() {
        System.out.println();
        System.out.println("-- Available Cars --");

        List<Car> cars = BOOKING_SERVICE.getAvailableCars();
        if (cars.size() == 0) {
            System.out.println("No cars available.");
            return;
        }
        for (int i = 0; i < cars.size(); i++) {
            printCar(cars.get(i), i + 1);
        }
    }

    private static void viewElectricCars() {
        System.out.println();
        System.out.println("-- Available Electric Cars --");

        List<Car> cars = CAR_SERVICE.getElectricCars();

        if (cars.size() == 0) {
            System.out.println("No available electric cars.");
            return;
        }
        int idx = 1;
        for (Car c : cars) {
            printCar(c, idx++);
        }
    }

    private static void viewAllUsers() {
        System.out.println();
        System.out.println("-- All Users --");

        List<User> users = USER_SERVICE.getUsers();
        for (int i = 0; i < users.size(); i++) {
            printUser(users.get(i), i + 1);
        }
    }

    private static User pickUser() {
        List<User> users = USER_SERVICE.getUsers();
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return null;
        }
        System.out.println("Users:");
        for (int i = 0; i < users.size(); i++) {
            printUser(users.get(i), i + 1);
        }
        int choice = readIntInRange("Pick a user: ", 1, users.size());
        return users.get(choice - 1);
    }

    private static int readMenuChoice(int min, int max) {
        return readIntInRange("Choose: ", min, max);
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Please use format YYYY-MM-DD.");
            }
        }
    }

    private static List<CarBooking> nonNull(List<CarBooking> list) {
        List<CarBooking> out = new ArrayList<>();
        for (CarBooking b : list) {
            if (b != null) out.add(b);
        }
        return out;
    }

    private static void printCar(Car car, int idx) {
        String electric = car.isElectric() ? "electric" : "petrol";
        System.out.printf(
                "  %d) %-16s [%s, %s]  $%s/day%n",
                idx,
                car.getRegNumber(),
                car.getBrand(),
                electric,
                car.getRentalPricePerDay().toPlainString()
        );
    }

    private static void printUser(User user, int idx) {
        System.out.printf("  %d) %-12s (%s)%n", idx, user.getName(), shortId(user.getId().toString()));
    }

    private static void printBooking(CarBooking b, int idx) {
        System.out.printf(
                "  %d) %s  %s -> %s  %s -> %s  $%s  %s%n",
                idx,
                shortId(b.getId().toString()),
                b.getUser().getName(),
                b.getCar().getRegNumber(),
                b.getStartDate(),
                b.getEndDate(),
                b.getPrice().toPlainString(),
                b.getStatus()
        );
    }

    private static String shortId(String uuid) {
        return uuid.length() > 8 ? uuid.substring(0, 8) : uuid;
    }
}
