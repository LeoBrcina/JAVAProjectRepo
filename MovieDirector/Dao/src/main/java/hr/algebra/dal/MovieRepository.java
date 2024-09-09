/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Korisnik
 */
public interface MovieRepository {
    
    int createMovie(Movie movie) throws Exception;

    void createMovies(List<Movie> movies) throws Exception;

    void updateMovie(int id, Movie data) throws Exception;

    void deleteMovie(int id) throws Exception;

    Optional<Movie> selectMovie(int id) throws Exception;
    
    Optional<Movie> selectMovie(String title) throws Exception;

    List<Movie> selectMovies() throws Exception;
        
    void createMovieActors (int movieId, List<Integer> actorIds) throws Exception;
    
    void createMovieDirectors (int movieId, List<Integer> directorIds) throws Exception;
    
    void createMovieGenres (int movieId, List<Integer> genreIds) throws Exception;
    
    void deleteMovieRelations (int movieId) throws Exception;
}
