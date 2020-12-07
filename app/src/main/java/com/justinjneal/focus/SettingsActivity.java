package com.justinjneal.focus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.ListPreference;

public class SettingsActivity extends PreferenceActivity {
    private ListPreference lp;

    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public static final String BREAK_TIME = "break_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        lp = (ListPreference) findPreference("sshort");

        savePreference();
    }

    public void savePreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(BREAK_TIME, lp.getValue());
    }

}