package com.example.battleships.Logic;

/**
 * Created by User on 02/01/2018.
 */

public class TableRow {
    private String name;
    private int score;
    private String location;
    private String difficulty;
    private boolean chosen;

    public TableRow(String name, int score, String location) {
        this.name = name;
        this.score = score;
        this.location = location;
        chosen = false;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int compareTo(TableRow row){
        return score-row.getScore();
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public TableRow setDifficultyAndGet(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableRow tableRow = (TableRow) o;
        if (score != tableRow.score) return false;
        if (name != null ? !name.equals(tableRow.name) : tableRow.name != null) return false;
        if (location != null ? !location.equals(tableRow.location) : tableRow.location != null)
            return false;
        return difficulty != null ? difficulty.equals(tableRow.difficulty) : tableRow.difficulty == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + score;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
        return result;
    }
}
