package bookmyshow.model;

public class Payment {
    private final String paymentId;
    private final String bookingId;
    private final double amount;
    private PaymentStatus status;

    public Payment(String paymentId, String bookingId, double amount) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = PaymentStatus.INITIATED;
    }

    public void pay() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
