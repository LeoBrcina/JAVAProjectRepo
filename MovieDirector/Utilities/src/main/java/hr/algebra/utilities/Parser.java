/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.utilities;

import hr.algebra.dal.GenericRepository;
import hr.algebra.dal.MovieRepository;
import hr.algebra.factory.ParserFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Genre;
import hr.algebra.model.Movie;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Korisnik
 */
public class Parser {
    
    private final MovieRepository movieRepository;
    private final GenericRepository<Actor> actorRepository;
    private final GenericRepository<Director> directorRepository;
    private final GenericRepository<Genre> genreRepository;

    private static final DateTimeFormatter PUB_DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    public Parser(MovieRepository movieRepository, GenericRepository<Actor> actorRepository, GenericRepository<Director> directorRepository, GenericRepository<Genre> genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.genreRepository = genreRepository;
    }

    public void parseAndSave(InputStream rssStream) throws XMLStreamException, SQLException, Exception {
        XMLEventReader eventReader = ParserFactory.createStaxParser(rssStream);

        Movie movie = null;
        List<String> actors = new ArrayList<>();
        List<String> directors = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String elementName = startElement.getName().getLocalPart();

                switch (elementName) {
                    case "item":
                        movie = new Movie();
                        actors.clear();
                        directors.clear();
                        genres.clear();
                        break;
                    case "title":
                    case "description":
                    case "pubDate":
                    case "plakat":
                    case "link":
                    case "glumci":
                    case "redatelj":
                    case "zanr":
                        String data = getCharacterData(eventReader);
                        if (movie != null) {
                            switch (elementName) {
                                case "title":
                                    movie.setTitle(data);
                                    break;
                                case "description":
                                    String description = extractTextAfterImage(data);
                                    if (description.isEmpty()) {
                                        movie.setDescription(null);
                                    } else {
                                        movie.setDescription(description);
                                    }
                                    break;
                                case "pubDate":
                                    movie.setPublishedDate(LocalDateTime.parse(data, PUB_DATE_FORMATTER)
                                            .atZone(ZoneId.systemDefault()).toLocalDateTime());
                                    break;
                                case "plakat":
                                    movie.setPicturePath(data);
                                    break;
                                case "link":
                                    movie.setLink(data);
                                    break;
                                case "glumci":
                                    if (!data.isEmpty()) {
                                        String[] actorNames = splitActorNames(data);
                                        for (String name : actorNames) {
                                            actors.add(name.trim());
                                        }
                                    }
                                    break;
                                case "redatelj":
                                    if (!data.isEmpty()) {
                                        String[] directorNames = data.split(", ");
                                        for (String name : directorNames) {
                                            directors.add(name.trim());
                                        }
                                    }
                                    break;
                                case "zanr":
                                    if (!data.isEmpty()) {
                                        String[] genreNames = data.split(", ");
                                        for (String name : genreNames) {
                                            genres.add(name.trim());
                                        }
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }

            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("item")) {
                if (movie != null) {
                    Optional<Movie> existingFilm = movieRepository.selectMovie(movie.getTitle());
                    if (existingFilm.isPresent()) {
                        updateFilm(existingFilm.get(), movie, actors, directors, genres);
                    } else {
                        saveFilmWithAssociations(movie, actors, directors, genres);
                    }
                }
            }
        }
    }

    private String getCharacterData(XMLEventReader eventReader) throws XMLStreamException {
        StringBuilder data = new StringBuilder();
        XMLEvent event = eventReader.nextEvent();
        while (event.isCharacters()) {
            Characters characters = event.asCharacters();
            data.append(characters.getData());
            event = eventReader.nextEvent();
        }
        return data.toString().trim();
    }

    private String extractTextAfterImage(String html) {
        String regex = "(?i)<img[^>]*>(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String text = matcher.group(1).trim();
            return text.replaceAll("<[^>]+>", "").trim();
        }
        return html.replaceAll("<[^>]+>", "").trim(); 
    }

    private String[] splitActorNames(String data) {
        data = data.replaceAll("\\si\\s", ", ");
        
        List<String> actorNames = new ArrayList<>();
        Matcher matcher = Pattern.compile("[^,\\s][^,]*[^,\\s]*").matcher(data);
        
        while (matcher.find()) {
            actorNames.add(matcher.group().trim());
        }
        
        return actorNames.toArray(new String[0]);
    }

    private void saveFilmWithAssociations(Movie movie, List<String> actors, List<String> directors, List<String> genres) throws SQLException {
        try {
            int movieId = movieRepository.createMovie(movie);

            for (String actorName : actors) {
                Actor actor = actorRepository.createByName(actorName);
                movieRepository.createMovieActors(movieId, List.of(actor.getId()));
            }

            for (String directorName : directors) {
                Director director = directorRepository.createByName(directorName);
                movieRepository.createMovieDirectors(movieId, List.of(director.getId()));
            }

            for (String genreName : genres) {
                Genre genre = genreRepository.createByName(genreName);
                movieRepository.createMovieGenres(movieId, List.of(genre.getId()));
            }
        } catch (Exception ex) {
            throw new SQLException("Error saving film with associations", ex);
        }
    }

    private void updateFilm(Movie existingMovie, Movie newMovieData, List<String> actors, List<String> directors, List<String> genres) throws SQLException {
        try {
            existingMovie.setDescription(newMovieData.getDescription());
            existingMovie.setPicturePath(newMovieData.getPicturePath());
            existingMovie.setLink(newMovieData.getLink());
            existingMovie.setPublishedDate(newMovieData.getPublishedDate());

            movieRepository.updateMovie(existingMovie.getId(), existingMovie);

            movieRepository.deleteMovieRelations(existingMovie.getId());

            for (String actorName : actors) {
                Actor actor = actorRepository.createByName(actorName);
                movieRepository.createMovieActors(existingMovie.getId(), List.of(actor.getId()));
            }

            for (String directorName : directors) {
                Director director = directorRepository.createByName(directorName);
                movieRepository.createMovieDirectors(existingMovie.getId(), List.of(director.getId()));
            }

            for (String genreName : genres) {
                Genre genre = genreRepository.createByName(genreName);
                movieRepository.createMovieGenres(existingMovie.getId(), List.of(genre.getId()));
            }
        } catch (Exception ex) {
            throw new SQLException("Error updating film with associations", ex);
        }
    }
    
}
