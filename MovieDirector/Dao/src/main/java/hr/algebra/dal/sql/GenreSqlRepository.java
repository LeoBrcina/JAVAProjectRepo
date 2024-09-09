/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.GenericRepository;
import hr.algebra.model.Genre;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author Korisnik
 */
public class GenreSqlRepository implements GenericRepository<Genre>{
    
    private static final String ID_GENRE = "IDGenre";
    private static final String NAME = "Name";

    private static final String CREATE = "{ CALL createGenre (?,?) }";
    private static final String UPDATE = "{ CALL updateGenre (?,?) }";
    private static final String DELETE = "{ CALL deleteGenre (?) }";
    private static final String SELECT = "{ CALL selectGenre (?) }";
    private static final String SELECT_ALL = "{ CALL selectGenres }";
    private static final String SELECT_BY_NAME = "{ CALL selectGenreByName (?) }";


    @Override
    public int create(Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        
        Optional<Genre> existingGenre = selectByName(genre.getName());
        if (existingGenre.isPresent()) {
            throw new Exception("A genre with the same name '" + genre.getName() + "' already exists.");
        }
        
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.setString(1, genre.getName());

            stmt.executeUpdate();
            genre.setId(stmt.getInt(ID_GENRE));
            return stmt.getInt(ID_GENRE);
        }
    }

    @Override
    public void create(List<Genre> genres) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            for (Genre genre : genres) {
                stmt.registerOutParameter(ID_GENRE, Types.INTEGER);
                stmt.setString(NAME, genre.getName());

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, Genre genre) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE)) {

            stmt.setInt(ID_GENRE, id);
            stmt.setString(NAME, genre.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE)) {

            stmt.setInt(ID_GENRE, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Genre> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT)) {

            stmt.setInt(ID_GENRE, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Genre(
                            rs.getInt(ID_GENRE),
                            rs.getString(NAME)
                    ));
            }
        }
        return Optional.empty();
    } 
    }

    @Override
    public List<Genre> selectAll() throws Exception {
        List<Genre> genres = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                genres.add(new Genre(
                        rs.getInt(ID_GENRE),
                        rs.getString(NAME)
                ));
        }
        return genres;
    }
}

    @Override
    public Optional<Genre> selectByName(String name) throws Exception {
        
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_BY_NAME)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Genre(
                            rs.getInt(ID_GENRE),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return Optional.empty();
        
    }

    @Override
    public Genre createByName(String name) throws Exception {
        Optional<Genre> genre = selectByName(name);
        if (genre.isPresent()) {
            return genre.get();
        } else {
            Genre newGenre = new Genre(name);
            create(newGenre);
            return newGenre;
        }    }
}

