package com.example.news_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,new MainSettingFragment()).addToBackStack(null).commit();

    }

    public static class MainSettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            bindSummaryValue(findPreference("key_full_name"));
            bindSummaryValue(findPreference("key_email"));
        }
    }
    public static void bindSummaryValue(Preference preference){
    preference.setOnPreferenceChangeListener(listener);
    listener.onPreferenceChange(preference,
            PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(),""));
    }


    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            if (preference instanceof ListPreference){

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index > 0?listPreference.getEntries()[index]
                        : null);
            }else if (preference instanceof EditTextPreference){
                preference.setSummary(stringValue);
            }return false;
        }

};}