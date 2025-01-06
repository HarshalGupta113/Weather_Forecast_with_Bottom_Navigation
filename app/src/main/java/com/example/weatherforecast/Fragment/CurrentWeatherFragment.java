package com.example.weatherforecast.Fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.LocationHelper;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherApiService;
import com.example.weatherforecast.WeatherData;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentWeatherFragment extends Fragment implements LocationHelper.LocationListener {

    private TextView currentLocation,dateTime,celsius,description,feelsLike,tempMinMax;
    ImageView weathericon;
    CardView cardView;
    private boolean isDataLoaded = false;
    private ProgressBar loadingProgressBar;

    public CurrentWeatherFragment() {
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
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        // Initialize views
        loadingProgressBar = view.findViewById(R.id.loading_progress);
        currentLocation=view.findViewById(R.id.currentLocation);
        cardView=view.findViewById(R.id.card);
        dateTime=view.findViewById(R.id.dateTime);
        celsius=view.findViewById(R.id.celsius);
        weathericon=view.findViewById(R.id.weatherImage);
        description=view.findViewById(R.id.description);
        feelsLike=view.findViewById(R.id.feelsLike);
        tempMinMax=view.findViewById(R.id.minMax);

        // Show progress bar while loading
        loadingProgressBar.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        // Fetch location and data only if not already loaded
        if (!isDataLoaded) {
            LocationHelper locationHelper = new LocationHelper(getActivity(), this);
            locationHelper.fetchLocation();
        }
        return view;
    }

    @Override
    public void onLocationFetched(double latitude, double longitude,String locationName) {
        // Pass the location to fetch weather data
        currentLocation.setText(locationName);
        fetchWeatherData(latitude, longitude);
    }

    @Override
    public void onLocationFetchFailed(String errorMessage) {
        // Handle failure (e.g., show an error message)
        Snackbar.make(requireView(),"Error :"+errorMessage,Snackbar.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void fetchWeatherData(double latitude, double longitude) {
        loadingProgressBar.setVisibility(View.VISIBLE);

        // Fetch weather data using the latitude and longitude
        WeatherApiService.fetchWeatherData(latitude, longitude, new WeatherApiService.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherData weatherData) {
                // Hide the progress bar and display weather data
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                    dateTime.setText(getCurrentDateTime());
                    celsius.setText(weatherData.getTemperature()+"°C");
                    String iconUrl = "https://openweathermap.org/img/wn/" + weatherData.getIcon() + "@2x.png";
                    Glide.with(requireContext()).load(iconUrl).into(weathericon);
                    description.setText(weatherData.getWeatherDescription());
                    tempMinMax.setText(weatherData.getTempMin()+"/"+weatherData.getTempMax());
                    feelsLike.setText("Feels like "+weatherData.getFeelLike());
                });
            }

            @Override
            public void onHourlySuccess(List<WeatherData> hourlyForecasts) {

            }

            @Override
            public void onFailure(String errorMessage) {
                // Hide the progress bar and display error message
                requireActivity().runOnUiThread(() -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    Snackbar.make(requireView(),"Error :"+errorMessage,Snackbar.LENGTH_SHORT).show();
                });
            }
        });
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
