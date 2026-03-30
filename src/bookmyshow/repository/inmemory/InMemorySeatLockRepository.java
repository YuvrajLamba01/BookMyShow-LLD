package bookmyshow.repository.inmemory;

import bookmyshow.model.SeatLock;
import bookmyshow.repository.SeatLockRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySeatLockRepository implements SeatLockRepository {
    private final Map<String, List<SeatLock>> locksByShowId = new HashMap<>();

    @Override
    public List<SeatLock> findByShowId(String showId) {
        return new ArrayList<>(locksByShowId.getOrDefault(showId, new ArrayList<>()));
    }

    @Override
    public Optional<SeatLock> findByShowIdAndUserId(String showId, String userId) {
        List<SeatLock> locks = locksByShowId.getOrDefault(showId, new ArrayList<>());
        for (SeatLock lock : locks) {
            if (lock.getUserId().equals(userId)) {
                return Optional.of(lock);
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(String showId, SeatLock lock) {
        locksByShowId.computeIfAbsent(showId, k -> new ArrayList<>()).add(lock);
    }

    @Override
    public void removeByShowIdAndUserId(String showId, String userId) {
        List<SeatLock> locks = locksByShowId.get(showId);
        if (locks == null) {
            return;
        }
        locks.removeIf(lock -> lock.getUserId().equals(userId));
    }

    @Override
    public void replaceAll(String showId, List<SeatLock> locks) {
        locksByShowId.put(showId, new ArrayList<>(locks));
    }

    @Override
    public List<String> findAllShowIds() {
        return new ArrayList<>(locksByShowId.keySet());
    }
}
