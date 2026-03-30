package bookmyshow.repository;

import bookmyshow.model.SeatLock;

import java.util.List;
import java.util.Optional;

public interface SeatLockRepository {
    List<SeatLock> findByShowId(String showId);

    Optional<SeatLock> findByShowIdAndUserId(String showId, String userId);

    void save(String showId, SeatLock lock);

    void removeByShowIdAndUserId(String showId, String userId);

    void replaceAll(String showId, List<SeatLock> locks);

    List<String> findAllShowIds();
}
