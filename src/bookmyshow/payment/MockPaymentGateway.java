package bookmyshow.payment;

public class MockPaymentGateway implements PaymentGateway {
    @Override
    public boolean charge(String bookingId, double amount) {
        return true;
    }

    @Override
    public boolean refund(String bookingId, double amount) {
        return true;
    }
}
