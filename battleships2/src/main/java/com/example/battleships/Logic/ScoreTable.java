package com.example.battleships.Logic;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 02/01/2018.
 */

public class ScoreTable {
    private LinkedList<TableRow> rows;
    private int size;
    private String difficulty;

    public ScoreTable(List<TableRow> rows, String difficulty) {
        this.rows = (LinkedList)rows;
        this.difficulty = difficulty;
        setToSize10();
        sortRows();
    }

    private void setToSize10() {
        if(rows.size()>Utility.TABLENUMOFROWS) {
            LinkedList<TableRow> tempRows = new LinkedList<TableRow>();
            for (int i = 0; i < Utility.TABLENUMOFROWS; i++)
                tempRows.add(rows.get(i));
            this.rows = tempRows;
        }else{
            for(int i = rows.size();i<Utility.TABLENUMOFROWS;i++)
                rows.add(new TableRow("",-1,"").setDifficultyAndGet(difficulty));
        }

    }


    public ScoreTable(String difficulty) {
        size = 0;
        rows = new LinkedList<TableRow>();
        this.difficulty = difficulty;
        for(int i = rows.size();i<Utility.TABLENUMOFROWS;i++)
            rows.add(new TableRow("",-1,"").setDifficultyAndGet(difficulty));
    }

    public boolean addRow(TableRow row){
        boolean added = false;
        int i;
        TableRow temp;
        row.setDifficulty(difficulty);
        for (i=0;(i<size)&&(rows.get(i).compareTo(row)>=0);i++);
        for (;(i<Utility.TABLENUMOFROWS);i++) {
            added = true;
            temp = rows.get(i);
            rows.set(i,row);
            row = temp;
        }
        if(size<Utility.TABLENUMOFROWS) {
            rows.add(row);
            added = true;
        }
        if(added)
            size++;
        return added;
    }

    public boolean addRow(String name, int score, String location){
        return addRow(new TableRow(name,score,location));
    }

    public TableRow getRow(int position) {
        return rows.get(position);
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void chooseRow(int position) {
        if(rows.get(position).getScore()!=-1) {
            chooseNone();
            rows.get(position).setChosen(true);
        }
    }

    public void chooseNone() {
        for (int i=0;i<rows.size();i++)
            rows.get(i).setChosen(false);
    }

    private void sortRows() {
        for(int i = 0;i<rows.size();i++)
            for(int j = 0;j<rows.size()-i-1;j++){
                if(rows.get(j).compareTo(rows.get(j+1)) < 0){
                    TableRow temp = rows.get(j);
                    rows.set(j,rows.get(j+1));
                    rows.set(j+1,temp);
                }
            }
    }
}
