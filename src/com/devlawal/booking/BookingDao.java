package src.com.devlawal.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingDao {
    private static List<Booking> bookings;

    static {
        bookings = new ArrayList<>();
    }

    // adds a booking to database
    public boolean addBooking(Booking booking) {
        return bookings.add(booking);
    }

    // returns all the available bookings
    public List<Booking> getAllBookings() {
        return bookings;

    }

    // deletes a booking
    public void deleteBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        for (Booking booking : bookings) {
            if (booking != null && booking.getBookingId() != null && booking.getBookingId().equals(bookingId)) {
                bookings.remove(booking);
                break;
            }
        }
    }
}
