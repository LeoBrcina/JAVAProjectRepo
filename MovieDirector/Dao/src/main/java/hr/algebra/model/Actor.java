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
public class Actor implements Comparable<Actor>{
    
    private int idActor;
    private String name;
    
    public Actor(){
    }

    public Actor(String name) {
        this.name = name;
    }

    public Actor(int idActor, String name) {
        this(name);
        this.idActor = idActor;
    }
    
    public int getId() {
        return idActor;
    }
    
    public void setId(int id){
        this.idActor = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return idActor + " - " + name;
    }

    @Override
    public int compareTo(Actor a) {
        return name.compareTo(a.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idActor, name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Actor actor = (Actor) obj;
        return idActor == actor.idActor && Objects.equals(name, actor.name);
    }
}
