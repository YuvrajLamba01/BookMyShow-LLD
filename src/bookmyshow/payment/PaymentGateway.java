package bookmyshow.payment;

public interface PaymentGateway {
    boolean charge(String bookingId, double amount);

    boolean refund(String bookingId, double amount);
}
