package bookmyshow.model;

import java.util.List;

public class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<String> seats;
    private final double totalAmount;
    private BookingStatus status;

    public Booking(String bookingId, User user, Show show, List<String> seats, double totalAmount) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
    }

    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public void fail() {
        this.status = BookingStatus.FAILED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<String> getSeats() {
        return seats;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
