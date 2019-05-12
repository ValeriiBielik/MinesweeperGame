package com.my.bielik.minesweeperapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvDifficulty;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDifficulty = findViewById(R.id.tv_difficulty);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

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

    private void checkDifficultyLevel(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getString(SettingsActivity.KEY_PREF_DIFFICULTIES_LIST, "beginner");

        tvDifficulty.setText(convertDifficultyToString(difficulty));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDifficultyLevel();
    }
}
