/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model.table;

import hr.algebra.model.Genre;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class GenreTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"ID", "Name"};
    private List<Genre> genres;

    public GenreTableModel(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int getRowCount() {
        return genres.size();
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
        Genre genre = genres.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return genre.getId(); 
            case 1:
                return genre.getName();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        fireTableDataChanged(); 
    }

    public Genre getGenreAt(int rowIndex) {
        return genres.get(rowIndex);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        fireTableRowsInserted(genres.size() - 1, genres.size() - 1); 
    }

    public void updateGenre(int rowIndex, Genre genre) {
        this.genres.set(rowIndex, genre);
        fireTableRowsUpdated(rowIndex, rowIndex); 
    }

    public void removeGenre(int rowIndex) {
        this.genres.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex); 
    }
}
