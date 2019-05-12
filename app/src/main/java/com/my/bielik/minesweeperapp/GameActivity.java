package com.my.bielik.minesweeperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener,
        GameFieldAdapter.Callback, GameFieldAdapter.OnSelectedButtonListener {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private GameFieldAdapter adapter;
    private ImageButton btnSimpleFlag, btnMineFlag;
    private TextView tvNumOfMines, tvTimeInfo;
    private Button btnReset;
    private boolean isMarked;
    private Game game;
    private GameWinnerDialog gameWinnerDialog;
    private int difficulty;

    private Timer timer;
    private MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        recyclerView = findViewById(R.id.field);
        tvNumOfMines = findViewById(R.id.mines_number);
        btnSimpleFlag = findViewById(R.id.button_simple_flag);
        btnMineFlag = findViewById(R.id.button_mine_flag);
        tvTimeInfo = findViewById(R.id.time);
        btnReset = findViewById(R.id.button_reset);

        btnSimpleFlag.setOnClickListener(this);
        btnMineFlag.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        gameWinnerDialog = new GameWinnerDialog();

        game = Game.getInstance();
        difficulty = Integer.parseInt(getIntent().getStringExtra("difficulty"));
        startGame();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_simple_flag:
                isMarked = false;
                btnSimpleFlag.setBackgroundResource(R.drawable.button_flag_focused);
                btnMineFlag.setBackgroundResource(R.drawable.button_flag_not_focused);
                break;
            case R.id.button_mine_flag:
                isMarked = true;
                btnMineFlag.setBackgroundResource(R.drawable.button_flag_focused);
                btnSimpleFlag.setBackgroundResource(R.drawable.button_flag_not_focused);
                break;
            case R.id.button_reset :
                tvTimeInfo.setText("00:00");
                startGame();
        }
    }

    @Override
    public boolean isCellMarked() {
        return isMarked;
    }

    @Override
    public void onMineFlagBtnSelected(int minesCounter) {
        tvNumOfMines.setText(String.valueOf(minesCounter));
    }

    @Override
    public void getFinishedGameStatus(boolean finishedGameStatus) {
        finishGame(finishedGameStatus);
    }

    private void startGame(){
        game.setDifficulty(difficulty);
        game.generateField();
        tvNumOfMines.setText(String.valueOf(game.getNumOfMines()));

        showField();
        startTimer();
    }

    private void finishGame(boolean finishedGameStatus){
        if (finishedGameStatus){
            Bundle bundle = new Bundle();
            bundle.putInt("difficulty", difficulty);
            bundle.putString("time", tvTimeInfo.getText().toString());
            gameWinnerDialog.setArguments(bundle);
            gameWinnerDialog.show(getSupportFragmentManager(), "Winner dialog");
        }
        endTimer();
    }

    private void showField(){
        adapter = new GameFieldAdapter(this, game);
        adapter.registerCallback(this);

        switch (difficulty){
            case 0 :
                layoutManager = new GridLayoutManager(this, 9);
                break;
            case 1 :
                layoutManager = new GridLayoutManager(this, 16);
                break;
            case 2 :
                layoutManager = new GridLayoutManager(this, 30);
                break;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void startTimer(){
        if (timer != null){
            timer.cancel();
        }

        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
    }

    private void endTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class MyTimerTask extends TimerTask {

        private int counter = 0;
        @Override
        public void run() {
            counter++;
            final String strTime = String.format(Locale.getDefault(),"%02d:%02d", counter / 60, counter % 60);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTimeInfo.setText(strTime);
                }
            });
        }
    }
}