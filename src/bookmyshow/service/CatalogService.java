package bookmyshow.service;

import bookmyshow.model.Show;
import bookmyshow.repository.ShowRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CatalogService {
    private final ShowRepository showRepository;

    public CatalogService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public List<Show> searchByMovie(String city, String title) {
        String normalizedCity = city.toLowerCase().trim();
        String normalizedTitle = title.toLowerCase().trim();

        return showRepository.findAll().stream()
                .filter(s -> s.getTheater().getCity().toLowerCase().equals(normalizedCity))
                .filter(s -> s.getMovie().getTitle().toLowerCase().equals(normalizedTitle))
                .collect(Collectors.toList());
    }

    public List<Show> searchByGenre(String city, String genre) {
        String normalizedCity = city.toLowerCase().trim();
        String normalizedGenre = genre.toLowerCase().trim();

        return showRepository.findAll().stream()
                .filter(s -> s.getTheater().getCity().toLowerCase().equals(normalizedCity))
                .filter(s -> s.getMovie().getGenre().toLowerCase().equals(normalizedGenre))
                .collect(Collectors.toList());
    }
}
