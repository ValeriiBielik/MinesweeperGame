package com.my.bielik.minesweeperapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.my.bielik.minesweeperapp.solverLogic.GameSolver;
import com.my.bielik.minesweeperapp.solverLogic.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFieldAdapter extends RecyclerView.Adapter<GameFieldAdapter.ViewHolder> {

    public static final String TAG = "GAME_SOLVER";

    private Game game;
    private List<Cell> cellList;
    private RecyclerView.LayoutParams params;

    private View parent;
    private Context context;
    private Callback callback;
    private OnSelectedButtonListener listener;

    private int minesCounter;
    private ViewHolder viewHolder;
    private int tag;
    ArrayList<Group> groups = new ArrayList<>();

    private boolean solverEnabled;
    private boolean endGameStatus;

    public interface Callback {
        boolean isCellMarked();
    }

    public interface OnSelectedButtonListener {
        void onMineFlagBtnSelected(int minesCounter);

        void getFinishedGameStatus(boolean finishedGameStatus);
    }

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageButton button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.image_button);
            button.setTag(tag);
            if (tag + 1 < game.getCellCount())
                tag++;
        }
    }

    public GameFieldAdapter(Context context, Game game, boolean solverEnabled) {
        this.context = context;
        this.game = game;
        this.cellList = game.getList();
        minesCounter = game.getNumOfMines();
        this.solverEnabled = solverEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, viewGroup, false);
        parent = viewGroup;

        switch (game.getDifficulty()) {
            case Game.Difficulty.EASY:
                params = new RecyclerView.LayoutParams(100, 100);
                break;
            case Game.Difficulty.MEDIUM:
                params = new RecyclerView.LayoutParams(60, 60);
                break;
            case Game.Difficulty.HARD:
                params = new RecyclerView.LayoutParams(34, 34);
                break;
        }
        view.setLayoutParams(params);
        listener = (OnSelectedButtonListener) context;
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

//        viewHolder.button.setLayoutParams(params);
//        viewHolder.button.setTag(i);
//        viewHolder.button.setId(i);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCellState(viewHolder.getAdapterPosition(), viewHolder.button);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cellList.size();
    }

    private void checkCellState(int position, ImageButton imageButton) {
        if (game.getList().get(position).isHidden()) {
            if (!callback.isCellMarked() && !game.getList().get(position).isMarked()) {   // simple flag on not marked cell
                if (game.getList().get(position).getState() == Cell.CellState.EMPTY) {
                    openCell(position, imageButton);
                } else if (game.getList().get(position).getState() == Cell.CellState.MINE) {
                    openAllCells();
                    listener.getFinishedGameStatus(false);
                    Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();
                }
            } else if (callback.isCellMarked() && !game.getList().get(position).isMarked()) {    // mine flag on not marked cell
                setMarker(position, imageButton);
            } else if (!callback.isCellMarked() && game.getList().get(position).isMarked()) {    // simple flag on marked cell
                game.getList().get(position).setMarked(false);
                if (game.getList().get(position).getState() == Cell.CellState.MINE) {
                    openAllCells();
                    listener.getFinishedGameStatus(false);
                    Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();
                } else {
                    openCell(position, imageButton);
                }
                minesCounter++;
                listener.onMineFlagBtnSelected(minesCounter);
            }
            if (solverEnabled) {
                solveGame();
            }
        }
    }

    private void openCell(int position, ImageButton imageButton) {
        if (game.getList().get(position).getState() == Cell.CellState.EMPTY) {
            game.getList().get(position).show();
            setBtnBackgroundAsEmptyCell(position, imageButton);
        } else {
            openAllCells();
            listener.getFinishedGameStatus(false);
            Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAllCells() {
        ImageButton button;
        for (int i = 0; i < cellList.size(); i++) {
            button = parent.findViewWithTag(i);
            if (cellList.get(i).isHidden()) {
                cellList.get(i).show();
                if (cellList.get(i).getState() == Cell.CellState.EMPTY && !cellList.get(i).isMarked()) {
                    setBtnBackgroundAsEmptyCell(i, button);
                } else if (cellList.get(i).getState() == Cell.CellState.EMPTY && cellList.get(i).isMarked()) {
                    button.setBackgroundResource(R.drawable.broken_flag);
                } else if (cellList.get(i).getState() == Cell.CellState.MINE && !cellList.get(i).isMarked()) {
                    button.setBackgroundResource(R.drawable.explosion);
                } else if (cellList.get(i).getState() == Cell.CellState.MINE && cellList.get(i).isMarked()) {
                    button.setBackgroundResource(R.drawable.flag);
                }
            }
        }
    }

    private void setBtnBackgroundAsEmptyCell(int position, ImageButton imageButton) {

        int x = position / game.getCellsCountY();
        int y = position % game.getCellsCountY();

        switch (game.getMinesNear(x, y)) {
            case 0:
                imageButton.setBackgroundResource(R.drawable.num_0);
                checkAllNullCells(x, y);
                break;
            case 1:
                imageButton.setBackgroundResource(R.drawable.num_1);
                break;
            case 2:
                imageButton.setBackgroundResource(R.drawable.num_2);
                break;
            case 3:
                imageButton.setBackgroundResource(R.drawable.num_3);
                break;
            case 4:
                imageButton.setBackgroundResource(R.drawable.num_4);
                break;
            case 5:
                imageButton.setBackgroundResource(R.drawable.num_5);
                break;
            case 6:
                imageButton.setBackgroundResource(R.drawable.num_6);
                break;
            case 7:
                imageButton.setBackgroundResource(R.drawable.num_7);
                break;
            case 8:
                imageButton.setBackgroundResource(R.drawable.num_8);
                break;
        }
    }

    private void checkAllNullCells(int x, int y) {
        ImageButton button;
        int tag;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((x + i) >= 0 && (y + j) >= 0 && (x + i) < game.getCellsCountX() && (y + j) < game.getCellsCountY()) {
                    if (game.getCell(x + i, y + j).isHidden()) {
                        if (game.getMinesNear(x + i, y + j) == 0) {
                            tag = (x + i) * game.getCellsCountY() + y + j;
                            game.getCell(x + i, y + j).show();
                            button = parent.findViewWithTag(tag);
                            button.setBackgroundResource(R.drawable.num_0);
                            checkAllNullCells(x + i, y + j);
                        } else if (game.getMinesNear(x + i, y + j) > 0) {
                            tag = (x + i) * game.getCellsCountY() + y + j;
                            game.getCell(x + i, y + j).show();
                            button = parent.findViewWithTag(tag);
                            setBtnBackgroundAsEmptyCell(tag, button);
                        }
                    }
                }
            }
        }
    }

    public void setMarker(int position, ImageButton imageButton) {
        if (minesCounter > 0 && !cellList.get(position).isMarked()) {
            game.getList().get(position).setMarked(true);
            imageButton.setBackgroundResource(R.drawable.flag);
            minesCounter--;
            listener.onMineFlagBtnSelected(minesCounter);
        }
        if (minesCounter == 0 && !endGameStatus) {
            if (gameEnded()) {
                openAllCells();
                endGameStatus = true;
                listener.getFinishedGameStatus(true);
                Toast.makeText(context, "You win", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void solveGame() {
        Log.e(TAG, "solveGame()");
        myAlgorithm();
    }


    private void myAlgorithm() {
        Log.e(TAG, "my algorithm()");

        boolean firstSetGroup = true;

        while (!gameEnded()) {
            if (firstSetGroup) {
                setGroups();
                firstSetGroup = openGroups();
            } else {
                correctPossibilities();
                openGroupWithPossibility();
                firstSetGroup = true;
            }
        }
    }

    private void setGroups() {
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

    private boolean openGroups() {
        Log.e(TAG, "open groups, groups num: " + groups.size());
        int id;
        ImageButton button;
        boolean success = false;

        for (Group group : groups) {
            if (group.getNumOfMines() == 0) {
                for (int i = 0; i < group.getHiddenCells().size(); i++) {
                    id = group.getHiddenCells().get(i).getId();
                    button = parent.findViewWithTag(id);
                    openCell(id, button);
                    success = true;
                }
            } else if (group.getNumOfMines() == group.getHiddenCells().size()) {
                for (int i = 0; i < group.getHiddenCells().size(); i++) {
                    id = group.getHiddenCells().get(i).getId();
                    button = parent.findViewWithTag(id);
                    setMarker(id, button);
                    success = true;
                }
            }
        }
        return success;
    }

    private boolean gameEnded() {
        Log.e(TAG, "check is game ended");
        int counter = game.getNumOfMines();

        for (int i = 0; i < cellList.size(); i++) {
            if (cellList.get(i).getState() == Cell.CellState.MINE){
                if (!cellList.get(i).isHidden()){
                    return true;
                }
                if (cellList.get(i).isMarked()) {
                    counter--;
                }
            }
        }

        if (counter == 0)
            return true;
        return false;
    }

    private void correctPossibilities() {
        Log.e(TAG, "correct possibilities");
        Map<Cell, Double> cells = new HashMap<>();
        // цикл устанавливает единое значение вероятности в каждой ячейке, учитывая различные значения вероятностей в ячейке от разных групп
        for (Group group : groups) {
            for (Cell cell : group.getHiddenCells()) {
                Double value;
                if ((value = cells.get(cell)) == null) // если ячейка еще не в мапе
                    cells.put(cell, (double) group.getNumOfMines() / group.size()); // то добавляем ее со значением из группы
                else     //если она уже в мапе, то корректируем ее значение по теории вероятности
                    cells.put(cell, GameSolver.Prob.sum(value, (double) group.getNumOfMines() / group.size()));
            }
        }
        // цикл корректирует значения с учетом того, что сумма вероятностей в группе должна быть равна количеству мин в группе
        boolean repeat;
        do {
            repeat = false;
            for (Group group : groups) {                                        // для каждой группы
                List<Double> prob = group.getProbabilities();                   //  берем список вероятностей всех ячеек в группе в процентах
                Double sum = 0.0;
                if (prob.size() != 0) {
                    for (Double elem : prob)
                        sum += elem;                                            //  вычисляем ее сумму
                    int mines = group.getNumOfMines();
                    if (Math.abs(sum - mines) > 1) {                            //  если разница между ними велика, то проводим корректировку
                        repeat = true;                                          //   фиксируем факт корректировки
                        GameSolver.Prob.correct(prob, mines);                   //   корректируем список
                        for (int i = 0; i < group.size(); i++) {                //   заносим откорректированные значения из списка в ячейки
                            double value = prob.get(i);
                            group.getHiddenCells().get(i).setProbability(value);
                        }
                    }
                }
            }
        }
        while (repeat);
        for (Cell cell : cells.keySet()) {  // перестраховка
            if (cell.getProbability() > 0.99) cell.setProbability(0.99);
            if (cell.getProbability() < 0) cell.setProbability(0.0);
        }
    }

    private void openGroupWithPossibility() {
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
            openCell(cellToOpen.getId(), (ImageButton) parent.findViewWithTag(cellToOpen.getId()));
    }

}