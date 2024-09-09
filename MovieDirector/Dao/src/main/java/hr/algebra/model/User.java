/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.util.Objects;


/**
 *
 * @author Korisnik
 */
public class User implements Comparable<User>{
    
    private int idUser;
    private String username;
    private String password;
    private boolean isAdmin;
    
    public User(int idUser, String username, String password, boolean isAdmin) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    public int getId() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRole() {
        return isAdmin;
    }

    public void setRole(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    @Override
    public String toString() {
        return idUser + " - " + username + " - " + password + " - " + isAdmin;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idUser, username, password, isAdmin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return idUser == user.idUser &&
               Objects.equals(username, user.username) &&
               Objects.equals(password, user.password) &&
               Objects.equals(isAdmin, user.isAdmin);
    }

    @Override
    public int compareTo(User user) {
        return username.compareTo(user.username);
    }
    
}
