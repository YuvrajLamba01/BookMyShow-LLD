package bookmyshow.repository;

import bookmyshow.model.Show;

import java.util.List;
import java.util.Optional;

public interface ShowRepository {
    Optional<Show> findById(String showId);

    List<Show> findAll();

    void save(Show show);
}
