package fr.isen.hugo.androiderestaurant.payment.demo.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import fr.isen.hugo.androiderestaurant.payment.demo.views.SummaryEditTestPreference;

import fr.isen.hugo.androiderestaurant.R;

public class SettingsFragment extends PreferenceFragment
        implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        onSharedPreferenceChanged(preferences, "paypal_payment_type");
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry());
        } else if (preference instanceof SummaryEditTestPreference) {
            preference.setSummary(preference.getSummary());
        }
    }
}
