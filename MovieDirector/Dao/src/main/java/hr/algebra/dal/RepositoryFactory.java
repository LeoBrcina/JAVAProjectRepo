/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Genre;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel.bele
 */
public final class RepositoryFactory {

    private static final Properties properties = new Properties();
    private static final String PATH = "/config/repository.properties";
    
    private static final String MOVIE_REPOSITORY = "MOVIE_REPOSITORY";
    private static final String ACTOR_REPOSITORY = "ACTOR_REPOSITORY";
    private static final String DIRECTOR_REPOSITORY = "DIRECTOR_REPOSITORY";
    private static final String GENRE_REPOSITORY = "GENRE_REPOSITORY";
    private static final String USER_REPOSITORY = "USER_REPOSITORY";

    private static MovieRepository movieRepository;
    private static GenericRepository<Actor> actorRepository;
    private static GenericRepository<Director> directorRepository;
    private static GenericRepository<Genre> genreRepository;
    private static UserRepository userRepository;

    static {
        try (InputStream is = RepositoryFactory.class.getResourceAsStream(PATH)) {
            properties.load(is);
            
            movieRepository = (MovieRepository) Class
                    .forName(properties.getProperty(MOVIE_REPOSITORY))
                    .getDeclaredConstructor()
                    .newInstance();
            
             actorRepository = (GenericRepository<Actor>) Class
                .forName(properties.getProperty(ACTOR_REPOSITORY))
                .getDeclaredConstructor()
                .newInstance();

        directorRepository = (GenericRepository<Director>) Class
                .forName(properties.getProperty(DIRECTOR_REPOSITORY))
                .getDeclaredConstructor()
                .newInstance();

        genreRepository = (GenericRepository<Genre>) Class
                .forName(properties.getProperty(GENRE_REPOSITORY))
                .getDeclaredConstructor()
                .newInstance();

        userRepository = (UserRepository) Class
                .forName(properties.getProperty(USER_REPOSITORY))
                .getDeclaredConstructor()
                .newInstance();
        
        } catch (Exception ex) {
            Logger.getLogger(RepositoryFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static MovieRepository getMovieRepository() {
        return movieRepository;
    }

    public static GenericRepository<Actor> getActorRepository() {
        return actorRepository;
    }
    
    public static GenericRepository<Director> getDirectorRepository() {
        return directorRepository;
    }
    
    public static GenericRepository<Genre> getGenreRepository() {
        return genreRepository;
    }
    
    public static UserRepository getUserRepository() {
        return userRepository;
    }
}
