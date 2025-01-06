package com.example.weatherforecast;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherforecast.Fragment.CurrentWeatherFragment;
import com.example.weatherforecast.Fragment.HourlyForecastFragment;
import com.example.weatherforecast.Fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Fragment currentWeatherFragment = new CurrentWeatherFragment();
    private Fragment hourlyForecastFragment = new HourlyForecastFragment();
    private Fragment settingsFragment = new SettingsFragment();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Apply saved theme before calling super.onCreate()
        applySavedTheme();
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpBottomNavigation();
        if (savedInstanceState == null) {
            replaceFragment(new CurrentWeatherFragment());
        }

    }
    private void setUpBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.item1) {
                selectedFragment = currentWeatherFragment;
            } else if (item.getItemId() == R.id.item2) {
                selectedFragment = hourlyForecastFragment;
            } else if (item.getItemId() == R.id.item3) {
                selectedFragment = settingsFragment;
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (existingFragment != null && existingFragment.isVisible()) {
            // Fragment already visible; no need to replace
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, fragment, tag)
                .commit();
    }
    private void applySavedTheme() {
        String savedTheme = sharedPreferences.getString("theme", "System Default");
        switch (savedTheme) {
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
        // Apply font size
        int fontSize = Integer.parseInt(sharedPreferences.getString("font_size", "2")); // Default: Medium (2)
        applyFontSize(fontSize);
    }
    private void applyFontSize(int fontSize) {
        float scaleFactor = 1.0f + (fontSize * 0.1f); // Adjust scale factor
        android.content.res.Configuration config = getResources().getConfiguration();
        config.fontScale = scaleFactor;

        // Apply updated configuration
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentLayout);
        if (currentFragment instanceof CurrentWeatherFragment) {
            LocationHelper locationHelper = new LocationHelper(this, (CurrentWeatherFragment) currentFragment);
            locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    
}