package com.my.bielik.minesweeperapp.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.my.bielik.minesweeperapp.activities.MainActivity;
import com.my.bielik.minesweeperapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference preference = findPreference(SettingsActivity.KEY_PREF_DIFFICULTIES_LIST);
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String selected = (String) o;
                Toast.makeText(getActivity(), MainActivity.convertDifficultyToString(selected), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        Preference switchPref = findPreference(SettingsActivity.KEY_PREF_SWITCH_GAME_SOLVER);
        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean selected = (boolean) o;
                String out;
                if (selected)
                    out = "selected";
                else
                    out = "not selected";
                Toast.makeText(getActivity(), out, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
