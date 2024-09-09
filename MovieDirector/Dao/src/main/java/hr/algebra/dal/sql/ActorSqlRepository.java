/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.GenericRepository;
import hr.algebra.model.Actor;
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
public class ActorSqlRepository implements GenericRepository<Actor>{
    
    private static final String ID_ACTOR = "IDActor";
    private static final String NAME = "Name";

    private static final String CREATE = "{ CALL createActor (?,?) }";
    private static final String UPDATE = "{ CALL updateActor (?,?) }";
    private static final String DELETE = "{ CALL deleteActor (?) }";
    private static final String SELECT = "{ CALL selectActor (?) }";
    private static final String SELECT_ALL = "{ CALL selectActors }";
    private static final String SELECT_BY_NAME = "{ CALL selectActorByName (?) }";

    @Override
    public int create(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        
        Optional<Actor> existingActor = selectByName(actor.getName());
        if (existingActor.isPresent()) {
            throw new Exception("An actor with the same name '" + actor.getName() + "' already exists.");
        }
        
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.setString(1, actor.getName());

            stmt.executeUpdate();
            actor.setId(stmt.getInt(ID_ACTOR));
            return stmt.getInt(ID_ACTOR);
        }
    }

    @Override
    public void create(List<Actor> actors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE)) {

            for (Actor actor : actors) {
                stmt.registerOutParameter(ID_ACTOR, Types.INTEGER);
                stmt.setString(NAME, actor.getName());

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void update(int id, Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE)) {

            stmt.setInt(ID_ACTOR, id);
            stmt.setString(NAME, actor.getName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE)) {

            stmt.setInt(ID_ACTOR, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Actor> select(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT)) {

            stmt.setInt(ID_ACTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Actor(
                            rs.getInt(ID_ACTOR),
                            rs.getString(NAME)
                    ));
            }
        }
        return Optional.empty();
    }
    }

    @Override
    public List<Actor> selectAll() throws Exception {
        List<Actor> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ALL); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ID_ACTOR),
                        rs.getString(NAME)
                ));
        }
    }
        return actors;
    }

    @Override
    public Optional<Actor> selectByName(String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_BY_NAME)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Actor(
                            rs.getInt(ID_ACTOR),
                            rs.getString(NAME)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Actor createByName(String name) throws Exception {
        Optional<Actor> actor = selectByName(name);
        if (actor.isPresent()) {
            return actor.get();
        } else {
            Actor newActor = new Actor(name);
            create(newActor);
            return newActor;
        }
    }
        
}
