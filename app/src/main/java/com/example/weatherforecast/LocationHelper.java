package com.example.weatherforecast;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.Manifest;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public interface LocationListener {
        void onLocationFetched(double latitude, double longitude, String locationName);
        void onLocationFetchFailed(String error);
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private Activity activity;
    private LocationListener listener;

    public LocationHelper(Activity activity, LocationListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void fetchLocation() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, location -> {
                        if (location != null) {
                            // Reverse geocode the location to get the location name
                            String locationName = getLocationName(location.getLatitude(), location.getLongitude());
                            listener.onLocationFetched(location.getLatitude(), location.getLongitude(), locationName);
                        } else {
                            listener.onLocationFetchFailed("Unable to fetch location.");
                        }
                    })
                    .addOnFailureListener(activity, e -> listener.onLocationFetchFailed("Failed to fetch location."));
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // You can change this to return other parts of the address (e.g., city, country)
                return address.getLocality();  // Returns the city or locality
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

    // Handle the result of the location permission request
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                listener.onLocationFetchFailed("Location permission denied.");
            }
        }
    }
}
