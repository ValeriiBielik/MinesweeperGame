package com.my.bielik.minesweeperapp.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.my.bielik.minesweeperapp.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_DIFFICULTIES_LIST = "difficulties_list";
    public static final String KEY_PREF_SWITCH_GAME_SOLVER = "switch_game_solver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
