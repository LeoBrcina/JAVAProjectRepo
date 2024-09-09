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
public class Genre implements Comparable<Genre>{
    
    private int idGenre;
    private String name;
    
    public Genre(){
    }

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int idGenre, String name) {
        this(name);
        this.idGenre = idGenre;
    }
    
    public void setId(int idGenre){
        this.idGenre=idGenre;
    }
    
    public int getId() {
        return idGenre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return idGenre + " - " + name;
    }
    
    @Override
    public int compareTo(Genre g) {
        return name.compareTo(g.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGenre, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Genre genre = (Genre) obj;
        return idGenre == genre.idGenre && Objects.equals(name, genre.name);
    }
}
