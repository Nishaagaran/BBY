package com.movie.management.service;

import com.movie.management.entity.Movie;
import com.movie.management.exception.MovieAlreadyExistsException;
import com.movie.management.exception.MovieNotFoundException;
import com.movie.management.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The Matrix");
        movie.setGenre("Sci-Fi");
        movie.setDirector("Wachowski Brothers");
        movie.setReleaseYear(1999);
        movie.setRating(8.7);

        movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Inception");
        movie2.setGenre("Sci-Fi");
        movie2.setDirector("Christopher Nolan");
        movie2.setReleaseYear(2010);
        movie2.setRating(8.8);
    }

    @Test
    void getAllMovies_ShouldReturnListOfMovies() {
        // Given
        List<Movie> movies = Arrays.asList(movie, movie2);
        when(movieRepository.findAll()).thenReturn(movies);

        // When
        List<Movie> result = movieService.getAllMovies();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovie() {
        // Given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // When
        Movie result = movieService.getMovieById(1L);

        // Then
        assertNotNull(result);
        assertEquals("The Matrix", result.getTitle());
        assertEquals(1999, result.getReleaseYear());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getMovieById_WhenMovieDoesNotExist_ShouldThrowException() {
        // Given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(999L));
        verify(movieRepository, times(1)).findById(999L);
    }

    @Test
    void createMovie_WhenMovieDoesNotExist_ShouldSaveAndReturnMovie() {
        // Given
        Movie newMovie = new Movie();
        newMovie.setTitle("Interstellar");
        newMovie.setGenre("Sci-Fi");
        newMovie.setDirector("Christopher Nolan");
        newMovie.setReleaseYear(2014);
        newMovie.setRating(8.6);

        when(movieRepository.existsByTitleAndReleaseYear("Interstellar", 2014)).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(newMovie);

        // When
        Movie result = movieService.createMovie(newMovie);

        // Then
        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository, times(1)).existsByTitleAndReleaseYear("Interstellar", 2014);
        verify(movieRepository, times(1)).save(newMovie);
    }

    @Test
    void createMovie_WhenMovieAlreadyExists_ShouldThrowException() {
        // Given
        when(movieRepository.existsByTitleAndReleaseYear("The Matrix", 1999)).thenReturn(true);

        // When & Then
        assertThrows(MovieAlreadyExistsException.class, () -> movieService.createMovie(movie));
        verify(movieRepository, times(1)).existsByTitleAndReleaseYear("The Matrix", 1999);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldUpdateAndReturnMovie() {
        // Given
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Matrix Reloaded");
        updatedMovie.setGenre("Sci-Fi");
        updatedMovie.setDirector("Wachowski Brothers");
        updatedMovie.setReleaseYear(2003);
        updatedMovie.setRating(7.2);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.findByTitleAndReleaseYear("The Matrix Reloaded", 2003))
                .thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // When
        Movie result = movieService.updateMovie(1L, updatedMovie);

        // Then
        assertNotNull(result);
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findByTitleAndReleaseYear("The Matrix Reloaded", 2003);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieDoesNotExist_ShouldThrowException() {
        // Given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(999L, movie));
        verify(movieRepository, times(1)).findById(999L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenTitleAndYearConflictWithAnotherMovie_ShouldThrowException() {
        // Given
        Movie conflictingMovie = new Movie();
        conflictingMovie.setTitle("Inception");
        conflictingMovie.setReleaseYear(2010);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.findByTitleAndReleaseYear("Inception", 2010))
                .thenReturn(Optional.of(movie2));

        // When & Then
        assertThrows(MovieAlreadyExistsException.class, 
                () -> movieService.updateMovie(1L, conflictingMovie));
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findByTitleAndReleaseYear("Inception", 2010);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_WhenMovieExists_ShouldDeleteMovie() {
        // Given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).delete(movie);

        // When
        movieService.deleteMovie(1L);

        // Then
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    void deleteMovie_WhenMovieDoesNotExist_ShouldThrowException() {
        // Given
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(999L));
        verify(movieRepository, times(1)).findById(999L);
        verify(movieRepository, never()).delete(any(Movie.class));
    }

    @Test
    void getMoviesByGenre_ShouldReturnListOfMovies() {
        // Given
        List<Movie> sciFiMovies = Arrays.asList(movie, movie2);
        when(movieRepository.findByGenre("Sci-Fi")).thenReturn(sciFiMovies);

        // When
        List<Movie> result = movieService.getMoviesByGenre("Sci-Fi");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findByGenre("Sci-Fi");
    }

    @Test
    void getMoviesByDirector_ShouldReturnListOfMovies() {
        // Given
        List<Movie> nolanMovies = Arrays.asList(movie2);
        when(movieRepository.findByDirector("Christopher Nolan")).thenReturn(nolanMovies);

        // When
        List<Movie> result = movieService.getMoviesByDirector("Christopher Nolan");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieRepository, times(1)).findByDirector("Christopher Nolan");
    }

    @Test
    void getMoviesByReleaseYear_ShouldReturnListOfMovies() {
        // Given
        List<Movie> moviesFrom1999 = Arrays.asList(movie);
        when(movieRepository.findByReleaseYear(1999)).thenReturn(moviesFrom1999);

        // When
        List<Movie> result = movieService.getMoviesByReleaseYear(1999);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("The Matrix", result.get(0).getTitle());
        verify(movieRepository, times(1)).findByReleaseYear(1999);
    }
}

