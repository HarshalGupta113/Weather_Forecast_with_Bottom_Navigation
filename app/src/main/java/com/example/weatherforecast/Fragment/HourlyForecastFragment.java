package com.example.weatherforecast.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.weatherforecast.DataModel.HourlyForecastAdapter;
import com.example.weatherforecast.LocationHelper;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherApiService;
import com.example.weatherforecast.WeatherData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HourlyForecastFragment extends Fragment implements LocationHelper.LocationListener {
    private RecyclerView recyclerView;
    private HourlyForecastAdapter adapter;
    private List<WeatherData> hourlyForecasts = new ArrayList<>();
    private boolean isDataLoaded = false;
    private ProgressBar progressBar;

    public HourlyForecastFragment() {
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
        View view = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_hourly_forecast);
        progressBar = view.findViewById(R.id.progress_circular_hour);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with an empty list
        adapter = new HourlyForecastAdapter(getContext(), hourlyForecasts);
        recyclerView.setAdapter(adapter);

        // Fetch location and data only if not already loaded
        if (!isDataLoaded) {
            LocationHelper locationHelper = new LocationHelper(getActivity(), this);
            locationHelper.fetchLocation();
        }
        return view;
    }

    @Override
    public void onLocationFetched(double latitude, double longitude, String locationName) {
        fetchHourlyData(latitude, longitude);
    }

    @Override
    public void onLocationFetchFailed(String error) {
        Snackbar.make(requireView(), "Error: " + error, Snackbar.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE); // Hide loading on error
    }

    public void fetchHourlyData(double latitude, double longitude) {
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator
        WeatherApiService.fetchHourlyData(latitude, longitude, new WeatherApiService.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherData weatherData) {
                // This won't be used for hourly data
            }

            @Override
            public void onHourlySuccess(List<WeatherData> hourlyData) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide loading indicator
                    hourlyForecasts.clear();
                    hourlyForecasts.addAll(hourlyData);
                    adapter.notifyDataSetChanged();
                    isDataLoaded = true;
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide loading indicator
                    Snackbar.make(requireView(), "Error: " + errorMessage, Snackbar.LENGTH_SHORT).show();
                });
            }
        });
    }
}
