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
public class Director implements Comparable<Director>{
    
    private int idDirector;
    private String name;
    
    public Director(){
    }

    public Director(String name) {
        this.name = name;
    }

    public Director(int idDirector, String name) {
        this(name);
        this.idDirector = idDirector;
    }
    
    public int getId() {
        return idDirector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return idDirector + " - " + name;
    }
    
    @Override
    public int compareTo(Director d) {
        return name.compareTo(d.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDirector, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Director director = (Director) obj;
        return idDirector == director.idDirector && Objects.equals(name, director.name);
    }

    public void setId(int idDirector){
        this.idDirector=idDirector;
    }


}
