/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;


import java.util.List;
import java.util.Optional;
/**
 *
 * @author Korisnik
 */
public interface GenericRepository<T> {
    
    int create(T entity) throws Exception;

    void create(List<T> entities) throws Exception;

    void update(int id, T data) throws Exception;

    void delete(int id) throws Exception;

    Optional<T> select(int id) throws Exception;

    List<T> selectAll() throws Exception;
    
    Optional<T> selectByName(String name) throws Exception;
    
    T createByName (String name) throws Exception;
}
