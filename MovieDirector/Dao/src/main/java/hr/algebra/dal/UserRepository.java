/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Korisnik
 */
public interface UserRepository {
    
    Optional<User> selectUser(int id) throws Exception;

    List<User> selectUsers() throws Exception;
    
    Optional<User> loginUser(String username, String password) throws Exception;
    
    int registerUser(User user) throws Exception;
    
    boolean userExists (String username) throws Exception;
    
    void cleanDatabase () throws Exception;
    
}
