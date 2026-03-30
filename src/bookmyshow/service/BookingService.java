package bookmyshow.service;

import bookmyshow.model.Booking;
import bookmyshow.model.BookingStatus;
import bookmyshow.model.Payment;
import bookmyshow.model.PaymentStatus;
import bookmyshow.model.SeatLock;
import bookmyshow.model.Show;
import bookmyshow.model.User;
import bookmyshow.repository.BookingRepository;
import bookmyshow.repository.SeatLockRepository;
import bookmyshow.repository.ShowRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BookingService {
    private static final int LOCK_MINUTES = 5;

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final SeatLockRepository seatLockRepository;
    private final PaymentService paymentService;

    public BookingService(
            ShowRepository showRepository,
            BookingRepository bookingRepository,
            SeatLockRepository seatLockRepository,
            PaymentService paymentService
    ) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.seatLockRepository = seatLockRepository;
        this.paymentService = paymentService;
    }

    public synchronized void releaseExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        for (String showId : seatLockRepository.findAllShowIds()) {
            Optional<Show> showOpt = showRepository.findById(showId);
            if (showOpt.isEmpty()) {
                continue;
            }
            Show show = showOpt.get();
            List<SeatLock> existingLocks = seatLockRepository.findByShowId(showId);

            List<SeatLock> validLocks = new ArrayList<>();
            for (SeatLock lock : existingLocks) {
                if (lock.isExpired(now)) {
                    show.getAvailableSeats().addAll(lock.getSeats());
                } else {
                    validLocks.add(lock);
                }
            }
            seatLockRepository.replaceAll(showId, validLocks);
        }
    }

    public synchronized boolean lockSeats(User user, String showId, List<String> seats) {
        releaseExpiredLocks();

        if (user == null || showId == null || showId.isBlank() || seats == null || seats.isEmpty()) {
            return false;
        }

        Optional<Show> showOpt = showRepository.findById(showId);
        if (showOpt.isEmpty()) {
            return false;
        }
        Show show = showOpt.get();

        if (!LocalDateTime.now().isBefore(show.getStartTime())) {
            return false;
        }

        Set<String> seatSet = new HashSet<>(seats);
        if (!show.getAvailableSeats().containsAll(seatSet)) {
            return false;
        }

        show.getAvailableSeats().removeAll(seatSet);

        SeatLock lock = new SeatLock(
                user.getUserId(),
                showId,
                seatSet,
                LocalDateTime.now().plusMinutes(LOCK_MINUTES)
        );
        seatLockRepository.save(showId, lock);
        return true;
    }

    public synchronized Optional<Booking> confirmBooking(User user, String showId) {
        releaseExpiredLocks();

        if (user == null || showId == null || showId.isBlank()) {
            return Optional.empty();
        }

        Optional<Show> showOpt = showRepository.findById(showId);
        if (showOpt.isEmpty()) {
            return Optional.empty();
        }
        Show show = showOpt.get();

        Optional<SeatLock> userLockOpt = seatLockRepository.findByShowIdAndUserId(showId, user.getUserId());
        if (userLockOpt.isEmpty()) {
            return Optional.empty();
        }

        SeatLock userLock = userLockOpt.get();
        LocalDateTime now = LocalDateTime.now();
        if (userLock.isExpired(now)) {
            show.getAvailableSeats().addAll(userLock.getSeats());
            seatLockRepository.removeByShowIdAndUserId(showId, user.getUserId());
            return Optional.empty();
        }

        List<String> bookedSeats = new ArrayList<>(userLock.getSeats());
        bookedSeats.sort(String::compareTo);
        double amount = bookedSeats.size() * show.getPricePerSeat();

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                user,
                show,
                bookedSeats,
                amount
        );

        Payment payment = paymentService.payForBooking(booking);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            booking.confirm();
            bookingRepository.save(booking);
            seatLockRepository.removeByShowIdAndUserId(showId, user.getUserId());
            return Optional.of(booking);
        }

        booking.fail();
        show.getAvailableSeats().addAll(userLock.getSeats());
        seatLockRepository.removeByShowIdAndUserId(showId, user.getUserId());
        return Optional.empty();
    }

    public synchronized boolean cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            return false;
        }

        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false;
        }

        Booking booking = bookingOpt.get();
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return false;
        }

        booking.cancel();
        booking.getShow().getAvailableSeats().addAll(booking.getSeats());
        paymentService.refundBooking(booking);
        bookingRepository.save(booking);
        return true;
    }
}
