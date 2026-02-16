package com.devlawal.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingDao {
    private final List<Booking> bookings;

    public BookingDao() {
        this.bookings = new ArrayList<>();
    }

    // Adds a booking to database
    public boolean addBooking(Booking booking) {
        return bookings.add(booking);
    }

    // Returns all the available bookings
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings); // Return defensive copy
    }

    // Deletes a booking
    public void deleteBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        bookings.removeIf(booking -> bookingId.equals(booking.getBookingId()));
    }
}
