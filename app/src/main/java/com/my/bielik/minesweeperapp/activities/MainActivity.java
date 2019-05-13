package com.my.bielik.minesweeperapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.my.bielik.minesweeperapp.R;
import com.my.bielik.minesweeperapp.dialogs.ScoreDialog;
import com.my.bielik.minesweeperapp.settings.SettingsActivity;

import static com.my.bielik.minesweeperapp.ScoreSaver.BEST_SCORES;
import static com.my.bielik.minesweeperapp.ScoreSaver.SCORES_AMATEUR_DIFFICULTY;
import static com.my.bielik.minesweeperapp.ScoreSaver.SCORES_BEGINNER_DIFFICULTY;
import static com.my.bielik.minesweeperapp.ScoreSaver.SCORES_PROFESSIONAL_DIFFICULTY;

public class MainActivity extends AppCompatActivity {

    private TextView tvDifficulty;
    private String difficulty;
    private ScoreDialog scoreDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDifficulty = findViewById(R.id.tv_difficulty);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        scoreDialog = new ScoreDialog();

    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

    public void openSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showScores(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("difficulty", difficulty);
        String[] scoreArray = new String[3];
        switch (Integer.parseInt(difficulty)) {
            case 0:
                for (int i = 0; i < 3; i++) {
                    scoreArray[i] = getSharedPreferences(BEST_SCORES, MODE_PRIVATE)
                            .getString(SCORES_BEGINNER_DIFFICULTY.concat(String.valueOf(i)), "");
                }
                break;
            case 1:
                for (int i = 0; i < 3; i++) {
                    scoreArray[i] = getSharedPreferences(BEST_SCORES, MODE_PRIVATE)
                            .getString(SCORES_AMATEUR_DIFFICULTY.concat(String.valueOf(i)), "");
                }
                break;
            case 2:
                for (int i = 0; i < 3; i++) {
                    scoreArray[i] = getSharedPreferences(BEST_SCORES, MODE_PRIVATE)
                            .getString(SCORES_PROFESSIONAL_DIFFICULTY.concat(String.valueOf(i)), "");
                }
                break;
        }
        bundle.putStringArray("score_set", scoreArray);

        scoreDialog.setArguments(bundle);
        scoreDialog.show(getSupportFragmentManager(), "score_dialog");
    }

    public static String convertDifficultyToString(String difficulty) {
        switch (Integer.parseInt(difficulty)) {
            case 0:
                return "beginner";
            case 1:
                return "amateur";
            case 2:
                return "professional";
            default:
                return "beginner";
        }
    }

    private void checkDifficultyLevel() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getString(SettingsActivity.KEY_PREF_DIFFICULTIES_LIST, "0");

        tvDifficulty.setText(convertDifficultyToString(difficulty));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDifficultyLevel();
    }
}
