package hr.algebra.dal.sql;

import hr.algebra.dal.MovieRepository;
import hr.algebra.model.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class MovieSqlRepository implements MovieRepository {

    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String PICTURE_PATH = "PicturePath";
    private static final String LINK = "Link";
    
    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_BY_TITLE = "{ CALL selectMovieByName (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    
    private static final String CREATE_MOVIE_ACTOR = "{ CALL createMovieActor (?, ?) }";
    private static final String SELECT_MOVIE_ACTORS = "{ CALL selectMovieActors (?) }";

    private static final String CREATE_MOVIE_DIRECTOR = "{ CALL createMovieDirector (?, ?) }";
    private static final String SELECT_MOVIE_DIRECTORS = "{ CALL selectMovieDirectors (?) }";

    private static final String CREATE_MOVIE_GENRE = "{ CALL createMovieGenre (?, ?) }";
    private static final String SELECT_MOVIE_GENRES = "{ CALL selectMovieGenres (?) }";
    
    private static final String DELETE_MOVIE_RELATIONS = "{ CALL deleteMovieRelations(?) }";

    @Override
    public int createMovie(Movie movie) throws Exception {
        int movieId;
        DataSource dataSource = DataSourceSingleton.getInstance();
    
    Optional<Movie> existingMovie = selectMovie(movie.getTitle());
    if (existingMovie.isPresent()) {
        throw new Exception("A movie with the same title '" + movie.getTitle() + "' already exists.");
    }
    
    try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

        stmt.registerOutParameter(6, Types.INTEGER);  
        stmt.setString(1, movie.getTitle());  
        stmt.setString(2, movie.getDescription());  
        stmt.setString(3, movie.getPublishedDate().format(Movie.DATE_FORMATTER));  
        stmt.setString(4, movie.getPicturePath());  
        stmt.setString(5, movie.getLink());  
        
        stmt.executeUpdate();
        movieId = stmt.getInt(6);

        createMovieActors(movieId, movie.getActorIds());
        createMovieDirectors(movieId, movie.getDirectorIds());
        createMovieGenres(movieId, movie.getGenreIds());
    }
        return movieId;
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            for (Movie movie : movies) {
                
                Optional<Movie> existingMovie = selectMovie(movie.getTitle());
                if (existingMovie.isPresent()) {
                    throw new Exception("A movie with the same title '" + movie.getTitle() + "' already exists.");
                }
                
                stmt.registerOutParameter(6, Types.INTEGER);
                stmt.setString(1, movie.getTitle());
                stmt.setString(2, movie.getDescription());
                stmt.setString(3, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
                stmt.setString(4, movie.getPicturePath());
                stmt.setString(5, movie.getLink());

                stmt.executeUpdate();
                int movieId = stmt.getInt(6);

                createMovieActors(movieId, movie.getActorIds());
                createMovieDirectors(movieId, movie.getDirectorIds());
                createMovieGenres(movieId, movie.getGenreIds());
            }
        }
    }

    @Override
    public void updateMovie(int id, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        
        Optional<Movie> existingMovie = selectMovie(movie.getTitle());
        if (existingMovie.isPresent() && existingMovie.get().getId() != id) {
            throw new Exception("A movie with the same title '" + movie.getTitle() + "' already exists.");
        }
        
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setInt(1, id);
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setString(5, movie.getPicturePath());
            stmt.setString(6, movie.getLink());

            stmt.executeUpdate();

            deleteMovieRelations(id);

            createMovieActors(id, movie.getActorIds());
            createMovieDirectors(id, movie.getDirectorIds());
            createMovieGenres(id, movie.getGenreIds());
        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
            deleteMovieRelations(id);
            
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();

        }
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            LocalDateTime.parse(rs.getString(4), Movie.DATE_FORMATTER),
                            rs.getString(5),
                            rs.getString(6)
                    );

                    movie.setActorIds(getIds(con, SELECT_MOVIE_ACTORS, id));
                    movie.setDirectorIds(getIds(con, SELECT_MOVIE_DIRECTORS, id));
                    movie.setGenreIds(getIds(con, SELECT_MOVIE_GENRES, id));

                    return Optional.of(movie);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIES); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        LocalDateTime.parse(rs.getString(4), Movie.DATE_FORMATTER),
                        rs.getString(5),
                        rs.getString(6)
                );

                movie.setActorIds(getIds(con, SELECT_MOVIE_ACTORS, movie.getId()));
                movie.setDirectorIds(getIds(con, SELECT_MOVIE_DIRECTORS, movie.getId()));
                movie.setGenreIds(getIds(con, SELECT_MOVIE_GENRES, movie.getId()));

                movies.add(movie);
            }
        }

        return movies;
    }

    public void createMovieActors(int movieId, List<Integer> actorIds) throws Exception{
        executeAssociationProcedure(movieId, actorIds, CREATE_MOVIE_ACTOR);
    }

    public void createMovieDirectors(int movieId, List<Integer> directorIds) throws Exception{
        executeAssociationProcedure(movieId, directorIds, CREATE_MOVIE_DIRECTOR);
    }

    public void createMovieGenres(int movieId, List<Integer> genreIds) throws Exception{
        executeAssociationProcedure(movieId, genreIds, CREATE_MOVIE_GENRE);
    }

    private void executeAssociationProcedure(int movieId, List<Integer> ids, String procedure) throws Exception{
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(procedure)) {
            for (int id : ids) {
                stmt.setInt(1, movieId);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
        }    
    }

    public void deleteMovieRelations(int id) throws Exception{
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_MOVIE_RELATIONS)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private List<Integer> getIds(Connection con, String query, int movieId) throws Exception{
List<Integer> ids = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(query)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
        }
        return ids;    }

    @Override
    public Optional<Movie> selectMovie(String title) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
    try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_BY_TITLE)) {

        stmt.setString(1, title); 
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(DESCRIPTION),
                        LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                        rs.getString(PICTURE_PATH),
                        rs.getString(LINK)
                );
                int id = movie.getId();
                
                movie.setActorIds(getIds(con, SELECT_MOVIE_ACTORS, id));
                movie.setDirectorIds(getIds(con, SELECT_MOVIE_DIRECTORS, id));
                movie.setGenreIds(getIds(con, SELECT_MOVIE_GENRES, id));

                return Optional.of(movie);
            }
        }
    }
    return Optional.empty();
    }
    
}
