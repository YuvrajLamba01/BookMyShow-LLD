package bookmyshow.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Show {
    private final String showId;
    private final Movie movie;
    private final Screen screen;
    private final Theater theater;
    private final LocalDateTime startTime;
    private final double pricePerSeat;
    private final Set<String> availableSeats;

    public Show(
            String showId,
            Movie movie,
            Screen screen,
            Theater theater,
            LocalDateTime startTime,
            double pricePerSeat
    ) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.theater = theater;
        this.startTime = startTime;
        this.pricePerSeat = pricePerSeat;
        this.availableSeats = new HashSet<>(screen.seatIds());
    }

    public boolean isRunning(LocalDateTime now) {
        LocalDateTime end = startTime.plusMinutes(movie.getDurationMins());
        return !now.isBefore(startTime) && !now.isAfter(end);
    }

    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public Theater getTheater() {
        return theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public Set<String> getAvailableSeats() {
        return availableSeats;
    }
}
