/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model.table;

import hr.algebra.model.Director;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class DirectorTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"ID", "Name"};
    private List<Director> directors;

    public DirectorTableModel(List<Director> directors) {
        this.directors = directors;
    }

    @Override
    public int getRowCount() {
        return directors.size();
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
        Director director = directors.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return director.getId(); 
            case 1:
                return director.getName(); 
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
        fireTableDataChanged(); 
    }

    public Director getDirectorAt(int rowIndex) {
        return directors.get(rowIndex);
    }

    public void addDirector(Director director) {
        this.directors.add(director);
        fireTableRowsInserted(directors.size() - 1, directors.size() - 1);
    }

    public void updateDirector(int rowIndex, Director director) {
        this.directors.set(rowIndex, director);
        fireTableRowsUpdated(rowIndex, rowIndex); 
    }

    public void removeDirector(int rowIndex) {
        this.directors.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex); 
    }
}
