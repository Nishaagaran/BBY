package com.movie.management.service;

import com.movie.management.entity.Movie;
import com.movie.management.exception.MovieAlreadyExistsException;
import com.movie.management.exception.MovieNotFoundException;
import com.movie.management.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
    }

    public Movie createMovie(Movie movie) {
        if (movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())) {
            throw new MovieAlreadyExistsException(
                "Movie already exists with title: " + movie.getTitle() + 
                " and release year: " + movie.getReleaseYear()
            );
        }
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        
        // Check if title and year combination already exists for a different movie
        Optional<Movie> existingMovie = movieRepository.findByTitleAndReleaseYear(
            movieDetails.getTitle(), 
            movieDetails.getReleaseYear()
        );
        
        if (existingMovie.isPresent() && !existingMovie.get().getId().equals(id)) {
            throw new MovieAlreadyExistsException(
                "Movie already exists with title: " + movieDetails.getTitle() + 
                " and release year: " + movieDetails.getReleaseYear()
            );
        }
        
        movie.setTitle(movieDetails.getTitle());
        movie.setGenre(movieDetails.getGenre());
        movie.setDirector(movieDetails.getDirector());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setRating(movieDetails.getRating());
        
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public List<Movie> getMoviesByDirector(String director) {
        return movieRepository.findByDirector(director);
    }

    public List<Movie> getMoviesByReleaseYear(Integer releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }
}

