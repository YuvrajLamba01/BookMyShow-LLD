package bookmyshow.model;

import java.time.LocalDateTime;
import java.util.Set;

public class SeatLock {
    private final String userId;
    private final String showId;
    private final Set<String> seats;
    private final LocalDateTime expiresAt;

    public SeatLock(String userId, String showId, Set<String> seats, LocalDateTime expiresAt) {
        this.userId = userId;
        this.showId = showId;
        this.seats = seats;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired(LocalDateTime now) {
        return !now.isBefore(expiresAt);
    }

    public String getUserId() {
        return userId;
    }

    public String getShowId() {
        return showId;
    }

    public Set<String> getSeats() {
        return seats;
    }
}
