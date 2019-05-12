package com.my.bielik.minesweeperapp;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

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
    }
}
