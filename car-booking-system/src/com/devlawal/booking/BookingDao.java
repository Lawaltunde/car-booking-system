package com.devlawal.booking;

import com.devlawal.user.User;

import java.util.UUID;

public class BookingDao {
    private static Booking[] bookings;
    private static int size = 0;

    static {
        bookings = new Booking[0];
    }

    public Booking[] getAllBookings() {
        return bookings;
    }

    public User checkIfUserIsBooked(UUID id) {
        if (id == null)
            return null;
        for (Booking booking : getAllBookings()) {
            if (booking == null || booking.getUser() == null || booking.getUser().getId() == null)
                continue;
            if (booking.getUser().getId().equals(id)) {
                return booking.getUser();
            }
            }
        return null;
        }

        public boolean addBooking(Booking booking) {
            if (size < bookings.length){
                bookings[size++] = booking;
                return true;
            }
            Booking[] moreBookings = new Booking[bookings.length + 5];
            int index = 0;
            for (Booking aBooking : bookings) {
                moreBookings[index++] = aBooking;
            }
            moreBookings[index] = booking;
            size++;
            bookings = moreBookings;
            return true;
        }

        // returns all the available bookings
        public Booking[] getAllBookingsWithoutNull() {
            int counter = 0;
            for (Booking abooking : getAllBookings()) {
                if (abooking != null) {
                    counter++;
                }
            }
            if (counter == 0)
                return new Booking[0];

            Booking[] availableBooking = new Booking[counter];
            int i = 0;
            for (Booking abooking : getAllBookings()) {
                if (abooking != null) {
                    availableBooking[i++] = abooking;
                }
            }
            return availableBooking;

        }
}
