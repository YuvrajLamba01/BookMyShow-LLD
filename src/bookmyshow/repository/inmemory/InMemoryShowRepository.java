package bookmyshow.repository.inmemory;

import bookmyshow.model.Show;
import bookmyshow.repository.ShowRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryShowRepository implements ShowRepository {
    private final Map<String, Show> shows = new HashMap<>();

    @Override
    public Optional<Show> findById(String showId) {
        return Optional.ofNullable(shows.get(showId));
    }

    @Override
    public List<Show> findAll() {
        return new ArrayList<>(shows.values());
    }

    @Override
    public void save(Show show) {
        shows.put(show.getShowId(), show);
    }
}
