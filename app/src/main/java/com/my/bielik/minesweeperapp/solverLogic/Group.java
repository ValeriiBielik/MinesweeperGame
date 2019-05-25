package com.my.bielik.minesweeperapp.solverLogic;

import com.my.bielik.minesweeperapp.gameLogic.Cell;

import java.util.ArrayList;

class Group {

    private int numOfMines;

    private ArrayList<Cell> hiddenCells = new ArrayList<>();
    private ArrayList<Cell> markedCells = new ArrayList<>();

    Group(int numOfMines) {
        this.numOfMines = numOfMines;
    }

    int getNumOfMines() {
        return numOfMines;
    }

    void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
    }

    void addHiddenCell(Cell cell) {
        hiddenCells.add(cell);
    }

    void addMarkedCell(Cell cell) {
        markedCells.add(cell);
    }

    ArrayList<Cell> getHiddenCells() {
        return hiddenCells;
    }

    ArrayList<Cell> getMarkedCells() {
        return markedCells;
    }

    int size() {
        return hiddenCells.size();
    }

    void checkMarks() {
        if (this.getMarkedCells().size() != 0) {
            this.setNumOfMines(this.getNumOfMines() - this.getMarkedCells().size());
            if (this.getHiddenCells().size() != 0) {
                for (int i = 0; i < this.getMarkedCells().size(); i++) {
                    for (int j = 0; j < this.getHiddenCells().size(); j++) {
                        if (this.getMarkedCells().get(i).getId() == this.getHiddenCells().get(j).getId()) {
                            this.getHiddenCells().remove(j);
                            break;
                        }
                    }
                }
            }

        }
    }

    ArrayList<Double> getProbabilities() {
        ArrayList<Double> probabilities = new ArrayList<>();
        if (this.getHiddenCells().size() != 0) {
            for (Cell cell : this.getHiddenCells()) {
                probabilities.add(cell.getProbability());
            }
        }
        return probabilities;
    }
}
