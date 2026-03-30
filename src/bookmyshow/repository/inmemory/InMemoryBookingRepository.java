package bookmyshow.repository.inmemory;

import bookmyshow.model.Booking;
import bookmyshow.repository.BookingRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> bookings = new HashMap<>();

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }
}
