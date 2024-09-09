/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model.table;

import hr.algebra.model.Movie;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class MovieTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"ID", "Title", "Published Date", "Picture Path", "Link", "Description"};
    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie movie = movies.get(rowIndex);
        switch (columnIndex) {
            case 0: return movie.getId();
            case 1: return movie.getTitle();
            case 2: return movie.getPublishedDate().format(movie.DATE_FORMATTER);
            case 3: return movie.getPicturePath();
            case 4: return movie.getLink();
            case 5: return movie.getDescription();
            default: throw new IllegalArgumentException("Invalid column index");
        }
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        fireTableRowsInserted(movies.size() - 1, movies.size() - 1);
    }

    public void updateMovie(int rowIndex, Movie movie) {
        movies.set(rowIndex, movie);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeMovie(int rowIndex) {
        movies.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Movie getMovieAt(int rowIndex) {
        return movies.get(rowIndex);
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged();  
    }
}
