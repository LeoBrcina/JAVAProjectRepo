/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.GenericRepository;
import hr.algebra.model.Director;
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
public class DirectorSqlRepository implements GenericRepository<Director>{

    private static final String ID_DIRECTOR = "IDDirector";
    private static final String NAME = "Name";

    private static final String CREATE = "{ CALL createDirector (?,?) }";
    private static final String UPDATE = "{ CALL createDirector (?,?) }";
    private static final String DELETE = "{ CALL deleteDirector (?) }";
    private static final String SELECT = "{ CALL selectDirector (?) }";
    private static final String SELECT_ALL = "{ CALL selectDirectors }";
    private static final String SELECT_BY_NAME = "{ CALL selectDirectorByName (?) }";


    @Override
    public int create(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        
        Optional<Director> existingDirector = selectByName(director.getName());
        if (existingDirector.isPresent()) {
            throw new Exception("A director with the same name '" + director.getName() + "' already exists.");
        }
        
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.setString(1, director.getName());
           
            stmt.executeUpdate();
            director.setId(stmt.getInt(ID_DIRECTOR));
            return stmt.getInt(ID_DIRECTOR);
        }
    }

    @Override
    public void create(List<Director> directors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            for (Director director : directors) {
                stmt.registerOutParameter(ID_DIRECTOR, Types.INTEGER);
                stmt.setString(NAME, director.getName());

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE)) {

            stmt.setInt(ID_DIRECTOR, id);
            stmt.setString(NAME, director.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE)) {

            stmt.setInt(ID_DIRECTOR, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Director> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT)) {

            stmt.setInt(ID_DIRECTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Director(
                            rs.getInt(ID_DIRECTOR),
                            rs.getString(NAME)
                    ));
            }
        }
        return Optional.empty();
    }
    }

    @Override
    public List<Director> selectAll() throws Exception {
        List<Director> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(ID_DIRECTOR),
                            rs.getString(NAME)
                ));
        }
        return directors;
        }}   

    @Override
    public Optional<Director> selectByName(String name) throws Exception {
        
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_BY_NAME)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Director(
                            rs.getInt(ID_DIRECTOR),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return Optional.empty();
        
    }

    @Override
    public Director createByName(String name) throws Exception {
        Optional<Director> director = selectByName(name);
    if (director.isPresent()) {
        return director.get();
    } else {
        Director newDirector = new Director(name);
        create(newDirector);
        return newDirector;
    }
    }
}   
    

