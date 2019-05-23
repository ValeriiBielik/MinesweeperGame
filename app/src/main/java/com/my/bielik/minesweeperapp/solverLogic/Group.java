package com.my.bielik.minesweeperapp.solverLogic;

import com.my.bielik.minesweeperapp.Cell;

import java.util.ArrayList;

public class Group {

    private int numOfMines;
    private ArrayList<Cell> hiddenCells = new ArrayList<>();
    private ArrayList<Cell> markedCells = new ArrayList<>();

    private Group() {}

    public Group(int numOfMines) {
        this.numOfMines = numOfMines;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
    }

    public void addHiddenCell(Cell cell) {
        hiddenCells.add(cell);
    }

    public void addMarkedCell(Cell cell) {
        markedCells.add(cell);
    }

    public ArrayList<Cell> getHiddenCells() {
        return hiddenCells;
    }

    public ArrayList<Cell> getMarkedCells() {
        return markedCells;
    }

    public int size() {
        return hiddenCells.size();
    }

    public void checkMarks() {
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

    public ArrayList<Double> getProbabilities() {
        ArrayList<Double> probabilities = new ArrayList<>();
        if (this.getHiddenCells().size() != 0) {
            for (Cell cell : this.getHiddenCells()) {
                probabilities.add(cell.getProbability());
            }
        }
        return probabilities;
    }


    public boolean contains(Group group) {
        if (this.hiddenCells.size() != 0 && group.hiddenCells.size() != 0) {
            int counter = group.hiddenCells.size();
            for (Cell parentCell : this.hiddenCells) {
                for (Cell childCell : group.hiddenCells) {
                    if (parentCell.getId() == childCell.getId()) {
                        counter--;
                        break;
                    }
                }
            }
            if (counter == 0)
                return true;
        }
        return false;
    }

    public void subtraction(Group group) {
        if (this.hiddenCells.size() != 0 && group.hiddenCells.size() != 0) {
            for (int i = 0; i < this.hiddenCells.size(); i++) {
                for (int j = 0; j < group.hiddenCells.size(); j++) {
                    if (this.hiddenCells.get(i).getId() == group.hiddenCells.get(j).getId()) {
                        this.hiddenCells.remove(i);
                        this.setNumOfMines(this.getNumOfMines() - 1);
                        break;
                    }
                }
            }
        }
    }

    public int overlaps(Group group) {
        int counter = 0;
        if (this.hiddenCells.size() != 0 && group.hiddenCells.size() != 0) {
            for (Cell parentCell : this.hiddenCells) {
                for (Cell childCell : group.hiddenCells) {
                    if (parentCell.getId() == childCell.getId()) {
                        counter++;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    public Group getOverlap(Group group) {
        if (this.hiddenCells.size() != 0 && group.hiddenCells.size() != 0) {
            int counter = group.hiddenCells.size();
            Group overlap = new Group();
            for (Cell parentCell : this.hiddenCells) {
                for (Cell childCell : group.hiddenCells) {
                    if (parentCell.getId() == childCell.getId()) {
                        overlap.addHiddenCell(parentCell);
                        counter--;
                        break;
                    }
                }
            }
            overlap.setNumOfMines(this.getNumOfMines() - counter);
            return overlap;
        }
        return null;
    }

    public boolean equals(Group group) {
        int size = -1;
        if (this.numOfMines == group.getNumOfMines()) {
            if (this.getHiddenCells().size() == group.getHiddenCells().size()) {
                size = this.hiddenCells.size();
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (this.getHiddenCells().get(i).getId() == group.getHiddenCells().get(j).getId()) {
                            size--;
                            break;
                        }
                    }
                }
            }
        }
        if (size == 0)
            return true;
        return false;
    }
}
