package bookmyshow.model;

public class Movie {
    private final String movieId;
    private final String title;
    private final int durationMins;
    private final String language;
    private final String genre;

    public Movie(String movieId, String title, int durationMins, String language, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.durationMins = durationMins;
        this.language = language;
        this.genre = genre;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMins() {
        return durationMins;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenre() {
        return genre;
    }
}
