package com.example.weatherforecast.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.weatherforecast.R;


public class SettingsFragment extends Fragment {
    private Spinner themeSpinner;
    private SeekBar fontSizeSeekBar;
    private SharedPreferences sharedPreferences;
    private boolean isThemeChanged = false;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        themeSpinner = view.findViewById(R.id.spinner_theme);
        fontSizeSeekBar = view.findViewById(R.id.seekbar_font_size);

        sharedPreferences = requireActivity().getSharedPreferences("AppSettings", 0);

        // Load saved preferences
        loadSettings();
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String theme = themeSpinner.getSelectedItem().toString();
                String currentTheme = sharedPreferences.getString("theme", "System Default");

                if (!theme.equals(currentTheme)) {
                    saveSetting("theme", theme);
                    isThemeChanged = true;
                    switch (theme) {
                        case "Light":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case "Dark":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        default:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // Font size adjustment
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saveSetting("font_size", String.valueOf(progress));

                // Example: Update font size dynamically
                float scaleFactor = 1.0f + (progress * 0.1f); // Adjust scale factor
                android.content.res.Configuration config = requireActivity().getResources().getConfiguration();
                config.fontScale = scaleFactor;

                requireActivity().getResources().updateConfiguration(
                        config,
                        requireActivity().getResources().getDisplayMetrics()
                );

                requireActivity().recreate(); // Apply changes globally
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        return view;
    }



    private void loadSettings() {
        // Load theme
        String theme = sharedPreferences.getString("theme", "System Default");
        int themeIndex = getThemeIndex(theme);
        themeSpinner.setSelection(themeIndex);

        // Load font size
        int fontSize = Integer.parseInt(sharedPreferences.getString("font_size", "2")); // Default: Medium (2)
        fontSizeSeekBar.setProgress(fontSize);

    }

    private int getThemeIndex(String theme) {
        switch (theme) {
            case "Light":
                return 0;
            case "Dark":
                return 1;
            default:
                return 2; // System Default
        }
    }

    private void saveSetting(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isThemeChanged) {
            requireActivity().recreate();
        }
    }
}