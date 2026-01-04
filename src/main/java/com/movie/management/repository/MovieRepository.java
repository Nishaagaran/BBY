package com.movie.management.repository;

import com.movie.management.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitleAndReleaseYear(String title, Integer releaseYear);

    List<Movie> findByGenre(String genre);

    List<Movie> findByDirector(String director);

    List<Movie> findByReleaseYear(Integer releaseYear);

    boolean existsByTitleAndReleaseYear(String title, Integer releaseYear);
}

