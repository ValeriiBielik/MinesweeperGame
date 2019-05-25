package com.my.bielik.minesweeperapp.gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private static final int SPAWN_CHANCE_OF_MINE = 15;

    private int difficulty;
    private int cellsCountX;
    private int cellsCountY;
    private int numOfMines;

    private Cell[][] field;
    private short[][] minesNear;
    private List<Cell> cellList;

    private static Game instance = new Game();

    private Game() {
    }

    public static Game getInstance() {
        return instance;
    }

    public void generateField() {
        initFieldSize();

        field = new Cell[cellsCountX][cellsCountY];
        minesNear = new short[cellsCountX][cellsCountY];
        cellList = new ArrayList<>();

        Random rnd = new Random();

        for (int x = 0; x < cellsCountX; x++) {
            for (int y = 0; y < cellsCountY; y++) {
                boolean isMine = rnd.nextInt(100) < SPAWN_CHANCE_OF_MINE;

                if (isMine) {
                    numOfMines++;
                    field[x][y] = new Cell(true, x * cellsCountY + y);
                    cellList.add(field[x][y]);
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if ((x + i >= 0) && (x + i < cellsCountX) && (y + j >= 0) && (y + j < cellsCountY)) {
                                minesNear[x + i][y + j]++;
                            }
                        }
                    }
                } else {
                    field[x][y] = new Cell(false, x * cellsCountY + y);
                    cellList.add(field[x][y]);
                }
            }
        }

    }

    public int getCellsCountX() {
        return cellsCountX;
    }

    public int getCellsCountY() {
        return cellsCountY;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<Cell> getList() {
        return cellList;
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public int getMinesNear(int x, int y) {
        return minesNear[x][y];
    }

    public int getCellCount() {
        return cellsCountX * cellsCountY;
    }

    private void initFieldSize() {
        switch (difficulty) {
            case Difficulty.EASY:
                cellsCountX = 9;
                cellsCountY = 9;
                break;
            case Difficulty.MEDIUM:
                cellsCountX = 16;
                cellsCountY = 16;
                break;
            case Difficulty.HARD:
                cellsCountX = 16;
                cellsCountY = 30;
                break;
        }
        numOfMines = 0;
    }

    public class Difficulty {
        public final static int EASY = 0;
        public final static int MEDIUM = 1;
        public final static int HARD = 2;
    }
}
