package src.com.devlawal.booking;

import java.util.UUID;

public class BookingDao {
    private static Booking[] bookings;
    private static int size = 0;

    static {
        bookings = new Booking[0];
    }

    // adds a booking to database
    public boolean addBooking(Booking booking) {
        if (size < bookings.length) {
            bookings[size++] = booking;
            booking.setBooked(true);
            return booking.isBooked();
        }
        Booking[] moreBookings = new Booking[bookings.length + 5];
        int index = 0;
        for (Booking aBooking : bookings) {
            moreBookings[index++] = aBooking;
        }
        moreBookings[index] = booking;
        size++;
        bookings = moreBookings;
        booking.setBooked(true);
        return booking.isBooked();
    }

    // returns all the available bookings
    public Booking[] getAllBookings() {
        int counter = 0;
        for (Booking abooking : bookings) {
            if (abooking != null) {
                counter++;
            }
        }
        if (counter == 0)
            return new Booking[0];

        Booking[] availableBooking = new Booking[counter];
        int i = 0;
        for (Booking abooking : bookings) {
            if (abooking != null) {
                availableBooking[i++] = abooking;
            }
        }
        return availableBooking;

    }

    // deletes a booking from the database
    public void deleteBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        int pos = 0;
        for (Booking booking : bookings) {
            if (booking != null && booking.getBookingId() != null && booking.getBookingId().equals(bookingId)) {
                bookings[pos] = null;
                break;
            }
            pos++;
        }
        for (int i = pos; i < (bookings.length - 1); i++) {
            bookings[i] = bookings[i + 1];
        }
        bookings[bookings.length - 1] = null;
        size--;
    }
}
