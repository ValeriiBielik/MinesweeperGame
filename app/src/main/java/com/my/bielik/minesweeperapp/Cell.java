package com.my.bielik.minesweeperapp;

public class Cell {
    private int id;
    private int state;
    private boolean isHidden;
    private boolean isMarked;
    private Double probability;

    public Cell(boolean isMine, int id){
        this.isHidden = true;
        this.isMarked = false;
        this.state = isMine ? CellState.MINE : CellState.EMPTY;
        this.id = id;
        probability = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHidden(){
        return isHidden;
    }

    public boolean isMarked(){
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public int getState() {
        return state;
    }

    public void show() {
        this.isHidden = false;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public class CellState {
        public static final int EMPTY = 0;
        public static final int MINE = 1;
    }
}
