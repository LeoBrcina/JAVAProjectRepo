/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.UserRepository;
import hr.algebra.model.User;
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
public class UserSqlRepository implements UserRepository{

    private static final String ID_USER = "IDUser";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String IS_ADMIN = "IsAdmin";
    
    private static final String REGISTER_USER = "{ CALL registerUser (?,?,?) }";
    private static final String LOGIN_USER = "{ CALL loginUser (?,?) }";
    private static final String SELECT_USER = "{ CALL selectUser(?) }";
    private static final String SELECT_ALL_USERS = "{ CALL selectAllUsers }";
    private static final String USER_EXISTS = "{ CALL userExists (?) }";
    
    private static final String CLEAN_DATABASE = "{call cleanDatabase}";
    
    @Override
    public Optional<User> loginUser(String username, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
    try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(LOGIN_USER)) {

        stmt.setString(1, username);
        stmt.setString(2, password);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) { 
                int loginSuccess = rs.getInt("LoginSuccess");
                if (loginSuccess == 1) {  
                    boolean isAdmin = rs.getBoolean("IsAdmin");

                    return fetchUserByUsername(username, isAdmin);
                }
            }
        }
    }
    return Optional.empty();
    }

    @Override
    public boolean userExists(String username) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(USER_EXISTS)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        }
        }
    }

    @Override
    public Optional<User> selectUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USER)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getBoolean(IS_ADMIN)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ALL_USERS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(ID_USER),
                        rs.getString(USERNAME),
                        rs.getString(PASSWORD),
                        rs.getBoolean(IS_ADMIN)
                ));
            }
        }
        return users;
    }

    @Override
    public int registerUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(REGISTER_USER)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.registerOutParameter(3, Types.INTEGER);


            stmt.executeUpdate();
            return stmt.getInt(ID_USER);
        }
    }

    private Optional<User> fetchUserByUsername(String username, boolean isAdmin) throws Exception{
        DataSource dataSource = DataSourceSingleton.getInstance();
    String sql = "{ CALL selectUser(?) }"; 
    try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(sql)) {
        stmt.setString(1, username);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("IDUser"),
                        rs.getString("Username"),
                        "", 
                        isAdmin
                ));
            }
        }
    }
    return Optional.empty();
    }

    @Override
    public void cleanDatabase() throws Exception {
        
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(CLEAN_DATABASE)) {

            stmt.execute();
        }
        
    }
}