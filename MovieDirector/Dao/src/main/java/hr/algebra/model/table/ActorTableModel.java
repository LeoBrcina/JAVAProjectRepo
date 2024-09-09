/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model.table;

import hr.algebra.model.Actor;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Korisnik
 */
public class ActorTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"ID", "Name"};
    private List<Actor> actors;

    public ActorTableModel(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public int getRowCount() {
        return actors.size();
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
        Actor actor = actors.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return actor.getId();
            case 1:
                return actor.getName(); 
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
        fireTableDataChanged(); 
    }

    public Actor getActorAt(int rowIndex) {
        return actors.get(rowIndex);
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
        fireTableRowsInserted(actors.size() - 1, actors.size() - 1); 
    }

    public void updateActor(int rowIndex, Actor actor) {
        this.actors.set(rowIndex, actor);
        fireTableRowsUpdated(rowIndex, rowIndex); 
    }

    public void removeActor(int rowIndex) {
        this.actors.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex); 
    }
}
