package bookmyshow.service;

import bookmyshow.model.Booking;
import bookmyshow.model.Payment;
import bookmyshow.payment.PaymentGateway;

import java.util.UUID;

public class PaymentService {
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Payment payForBooking(Booking booking) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                booking.getBookingId(),
                booking.getTotalAmount()
        );

        boolean ok = paymentGateway.charge(booking.getBookingId(), booking.getTotalAmount());
        if (ok) {
            payment.pay();
        }
        return payment;
    }

    public Payment refundBooking(Booking booking) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                booking.getBookingId(),
                booking.getTotalAmount()
        );
        boolean ok = paymentGateway.refund(booking.getBookingId(), booking.getTotalAmount());
        if (ok) {
            payment.refund();
        }
        return payment;
    }
}
