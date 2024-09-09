/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Korisnik
 */
public class Movie implements Comparable<Movie>{
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private int idMovie;
    private String title;
    private String description;
    private LocalDateTime publishedDate;
    private String picturePath;
    private String link;
    
    private List<Integer> actorIds;
    private List<Integer> directorIds;
    private List<Integer> genreIds;
    
    public Movie(){
        this.actorIds = new ArrayList<>();
        this.directorIds = new ArrayList<>();
        this.genreIds = new ArrayList<>();
    }
    
    public Movie(String title, String description, LocalDateTime publishedDate, String picturePath, String link) {
        this();
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        this.picturePath = picturePath;
        this.link = link;
    }
    
    public Movie(int idMovie, String title, String description, LocalDateTime publishedDate, String picturePath, String link) {
        this(title, description, publishedDate, picturePath, link);
        this.idMovie = idMovie;
    }
    
    public Movie(int idMovie, String title, String description, LocalDateTime publishedDate, String picturePath, String link, List<Integer> actorIds, List<Integer> directorIds, List<Integer> genreIds) {
        this(title, description, publishedDate, picturePath, link);
        this.idMovie = idMovie;
        this.actorIds = actorIds != null ? actorIds : new ArrayList<>();
        this.directorIds = directorIds != null ? directorIds : new ArrayList<>();
        this.genreIds = genreIds != null ? genreIds : new ArrayList<>();
    }
    
    public int getId() {
        return idMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public List<Integer> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<Integer> actorIds) {
        this.actorIds = actorIds != null ? actorIds : new ArrayList<>();
    }

    public List<Integer> getDirectorIds() {
        return directorIds;
    }

    public void setDirectorIds(List<Integer> directorIds) {
        this.directorIds = directorIds != null ? directorIds : new ArrayList<>();
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds != null ? genreIds : new ArrayList<>();
    }

    @Override
    public String toString() {
        return idMovie + " - " + title;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idMovie, title, description, publishedDate, picturePath, link);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movie movie = (Movie) obj;
        return idMovie == movie.idMovie &&
               Objects.equals(title, movie.title) &&
               Objects.equals(description, movie.description) &&
               Objects.equals(publishedDate, movie.publishedDate) &&
               Objects.equals(picturePath, movie.picturePath) &&
               Objects.equals(link, movie.link);
    }

    @Override
    public int compareTo(Movie m) {
        return title.compareTo(m.title);
    }
}
