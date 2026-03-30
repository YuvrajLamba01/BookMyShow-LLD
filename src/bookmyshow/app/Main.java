package bookmyshow.app;

import bookmyshow.model.Booking;
import bookmyshow.model.Movie;
import bookmyshow.model.Screen;
import bookmyshow.model.Show;
import bookmyshow.model.Theater;
import bookmyshow.model.User;
import bookmyshow.payment.MockPaymentGateway;
import bookmyshow.payment.PaymentGateway;
import bookmyshow.repository.BookingRepository;
import bookmyshow.repository.SeatLockRepository;
import bookmyshow.repository.ShowRepository;
import bookmyshow.repository.inmemory.InMemoryBookingRepository;
import bookmyshow.repository.inmemory.InMemorySeatLockRepository;
import bookmyshow.repository.inmemory.InMemoryShowRepository;
import bookmyshow.service.BookingService;
import bookmyshow.service.CatalogService;
import bookmyshow.service.PaymentService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {

    private static class AppContext {
        private final CatalogService catalogService;
        private final BookingService bookingService;
        private final User user;

        private AppContext(CatalogService catalogService, BookingService bookingService, User user) {
            this.catalogService = catalogService;
            this.bookingService = bookingService;
            this.user = user;
        }
    }

    private static AppContext setupSampleData() {
        User user = new User("u-1", "Yuvraj", "yuvraj@example.com");

        Movie movie = new Movie("m-1", "Interstellar", 169, "English", "Sci-Fi");
        Screen screen = new Screen("s-1", "Audi-1", 3, 5);

        Theater theater = new Theater("t-1", "PVR Orion", "Bangalore");
        theater.addScreen(screen);

        Show show = new Show(
                "sh-1",
                movie,
                screen,
                theater,
                LocalDateTime.now().plusHours(2),
                250.0
        );

            ShowRepository showRepository = new InMemoryShowRepository();
            showRepository.save(show);

            BookingRepository bookingRepository = new InMemoryBookingRepository();
            SeatLockRepository seatLockRepository = new InMemorySeatLockRepository();

            PaymentGateway paymentGateway = new MockPaymentGateway();
            PaymentService paymentService = new PaymentService(paymentGateway);

            CatalogService catalogService = new CatalogService(showRepository);
            BookingService bookingService = new BookingService(
                showRepository,
                bookingRepository,
                seatLockRepository,
                paymentService
            );

        return new AppContext(catalogService, bookingService, user);
    }

    private static void runDemo() {
        AppContext context = setupSampleData();

        List<Show> shows = context.catalogService.searchByMovie("Bangalore", "Interstellar");
        if (shows.isEmpty()) {
            System.out.println("No shows found");
            return;
        }

        Show show = shows.get(0);
        System.out.println("Show found: " + show.getShowId() + ", available seats: " + show.getAvailableSeats().size());

        boolean lockOk = context.bookingService.lockSeats(context.user, show.getShowId(), Arrays.asList("A1", "A2"));
        System.out.println("Seat lock: " + (lockOk ? "SUCCESS" : "FAILED"));

        Optional<Booking> booking = context.bookingService.confirmBooking(context.user, show.getShowId());
        if (booking.isEmpty()) {
            System.out.println("Booking failed");
            return;
        }

        System.out.println(
                "Booking confirmed: id=" + booking.get().getBookingId()
                        + ", seats=" + booking.get().getSeats()
                        + ", amount=" + booking.get().getTotalAmount()
        );

        boolean cancelled = context.bookingService.cancelBooking(booking.get().getBookingId());
        System.out.println("Cancelled: " + cancelled);
            System.out.println("Available seats after cancel: " + show.getAvailableSeats().size());
    }

    public static void main(String[] args) {
        runDemo();
    }
}
