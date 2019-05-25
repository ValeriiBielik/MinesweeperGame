package com.my.bielik.minesweeperapp.solverLogic;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.my.bielik.minesweeperapp.gameLogic.Cell;
import com.my.bielik.minesweeperapp.gameLogic.Game;
import com.my.bielik.minesweeperapp.GameFieldAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSolver {

    private static final String TAG = "GAME_SOLVER";

    private ArrayList<Group> groups = new ArrayList<>();
    private Game game;
    private View parent;

    public GameSolver(View view) {
        game = Game.getInstance();
        parent = view;
    }

    public void setGroups() {
        Log.e(TAG, "set groups, groups num: " + groups.size());
        groups.clear();
        for (int x = 0; x < game.getCellsCountX(); x++) {
            for (int y = 0; y < game.getCellsCountY(); y++) {
                if (!game.getCell(x, y).isHidden() && game.getMinesNear(x, y) > 0) {
                    Group group = new Group(game.getMinesNear(x, y));
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            if ((x + i >= 0) && (x + i < game.getCellsCountX()) && (y + j >= 0) && (y + j < game.getCellsCountY())) {
                                if (game.getCell(x + i, y + j).isHidden()) {
                                    if (game.getCell(x + i, y + j).isMarked())
                                        group.addMarkedCell(game.getCell(x + i, y + j));
                                    else
                                        group.addHiddenCell(game.getCell(x + i, y + j));
                                }
                            }
                        }
                    }
                    groups.add(group);
                }
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            group.checkMarks();
        }
    }

    public boolean openGroups(GameFieldAdapter adapter) {
        Log.e(TAG, "open groups, groups num: " + groups.size());
        int id;
        ImageButton button;
        boolean success = false;

        for (Group group : groups) {
            if (group.getNumOfMines() == 0) {
                for (int i = 0; i < group.getHiddenCells().size(); i++) {
                    id = group.getHiddenCells().get(i).getId();
                    button = parent.findViewWithTag(id);
                    adapter.openCell(id, button);
                    success = true;
                }
            } else if (group.getNumOfMines() == group.getHiddenCells().size()) {
                for (int i = 0; i < group.getHiddenCells().size(); i++) {
                    id = group.getHiddenCells().get(i).getId();
                    button = parent.findViewWithTag(id);
                    adapter.setMarker(id, button);
                    success = true;
                }
            }
        }
        return success;
    }

    public void correctPossibilities() {
        Log.e(TAG, "correct possibilities");
        Map<Cell, Double> cells = new HashMap<>();
        for (Group group : groups) {
            for (Cell cell : group.getHiddenCells()) {
                Double value;
                if ((value = cells.get(cell)) == null)
                    cells.put(cell, (double) group.getNumOfMines() / group.size());
                else
                    cells.put(cell, GameSolver.Prob.sum(value, (double) group.getNumOfMines() / group.size()));
            }
        }
        boolean repeat;
        do {
            repeat = false;
            for (Group group : groups) {
                List<Double> prob = group.getProbabilities();
                Double sum = 0.0;
                if (prob.size() != 0) {
                    for (Double elem : prob)
                        sum += elem;
                    int mines = group.getNumOfMines();
                    if (Math.abs(sum - mines) > 1) {
                        repeat = true;
                        GameSolver.Prob.correct(prob, mines);
                        for (int i = 0; i < group.size(); i++) {
                            double value = prob.get(i);
                            group.getHiddenCells().get(i).setProbability(value);
                        }
                    }
                }
            }
        }
        while (repeat);
        for (Cell cell : cells.keySet()) {
            if (cell.getProbability() > 0.99) cell.setProbability(0.99);
            if (cell.getProbability() < 0) cell.setProbability(0.0);
        }
    }

    public void openGroupWithPossibility(GameFieldAdapter adapter) {
        Log.e(TAG, "open group with possibility()");

        Cell cellToOpen = null;
        for (Group group : groups) {
            for (Cell cell : group.getHiddenCells()) {
                if (cellToOpen == null)
                    cellToOpen = cell;
                else if (cellToOpen.getProbability() < cell.getProbability())
                    cellToOpen = cell;
            }
        }

        if (cellToOpen == null) {
            for (Group group : groups) {
                for (Cell cell : group.getHiddenCells()) {
                    if (cell.isHidden())
                        cellToOpen = cell;
                }
            }
        }

        if (cellToOpen != null)
            adapter.openCell(cellToOpen.getId(), (ImageButton) parent.findViewWithTag(cellToOpen.getId()));
    }

    private static class Prob {

        private static Double sum(Double previousProb, Double currentProb) {
            return 1 - (1 - previousProb) * (1 - currentProb);
        }

        private static void correct(List<Double> probs, int mines) {
            Double sum = 0.0;
            if (probs.size() != 0) {
                for (Double prob : probs) {
                    sum += prob;
                }
                sum = mines / sum;
                for (int i = 0; i < probs.size(); i++) {
                    probs.set(i, probs.get(i) * sum);
                }
            }
        }
    }
}
