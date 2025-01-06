# Weather Forecast Application

## Overview

This is an Android application that provides real-time weather updates based on the user's location. The app fetches weather data using an API, displays current weather conditions, and features a clean and user-friendly interface.

## Features

- **Current Weather:** Displays temperature, weather description, and additional details (e.g., feels-like temperature, min/max temperatures).
- **Location-Based:** Automatically fetches weather information based on the user's current location.
- **Dynamic Icons:** Weather icons adjust for light and dark mode using the `app:tint` attribute.
- **Progress Indicator:** Shows a loading progress bar until data is fetched.
- **Error Handling:** Displays error messages via `Snackbar` if location or weather data cannot be retrieved.

## Technologies Used

- **Android SDK**
- **Java** for application logic
- **XML** for UI layouts
- **OpenWeather API** for fetching weather data
- **Glide** for image loading
- **Material Design Components** for modern UI elements
- **ConstraintLayout** for flexible and responsive layouts

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/weather-forecast-app.git
   ```
2. Open the project in Android Studio.
3. Add your OpenWeather API key:
   - Navigate to `WeatherApiService.java`.
   - Replace `YOUR_API_KEY` with your API key.
4. Build and run the application on an emulator or physical device.

## Code Highlights

### XML Layout (fragment_current_weather.xml)

```xml
<ImageView
    android:id="@+id/imageView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/outline_location_on_24"
    app:tint="?attr/colorOnBackground"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />

<ProgressBar
    android:id="@+id/loading_progress"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    android:visibility="gone" />
```

### Java Logic (CurrentWeatherFragment.java)

```java
@Override
public void onLocationFetched(double latitude, double longitude, String locationName) {
    loadingProgressBar.setVisibility(View.GONE);
    currentLocation.setText(locationName);
    fetchWeatherData(latitude, longitude);
}

private void fetchWeatherData(double latitude, double longitude) {
    WeatherApiService.fetchWeatherData(latitude, longitude, new WeatherApiService.WeatherDataCallback() {
        @Override
        public void onSuccess(WeatherData weatherData) {
            requireActivity().runOnUiThread(() -> {
                loadingProgressBar.setVisibility(View.GONE);
                dateTime.setText(getCurrentDateTime());
                celsius.setText(weatherData.getTemperature() + "\u00B0C");
                String iconUrl = "https://openweathermap.org/img/wn/" + weatherData.getIcon() + "@2x.png";
                Glide.with(requireContext()).load(iconUrl).into(weathericon);
                description.setText(weatherData.getWeatherDescription());
                tempMinMax.setText(weatherData.getTempMin() + "/" + weatherData.getTempMax());
                feelsLike.setText("Feels like " + weatherData.getFeelLike());
            });
        }

        @Override
        public void onFailure(String errorMessage) {
            requireActivity().runOnUiThread(() -> {
                loadingProgressBar.setVisibility(View.GONE);
                Snackbar.make(requireView(), "Error: " + errorMessage, Snackbar.LENGTH_SHORT).show();
            });
        }
    });
}
```

## Screenshots

### Light Mode

![Light Mode Screenshot](path/to/light-mode-screenshot.png)

### Dark Mode

![Dark Mode Screenshot](path/to/dark-mode-screenshot.png)

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes and commit them:
   ```bash
   git commit -m "Add your feature description"
   ```
4. Push to the branch:
   ```bash
   git push origin feature/your-feature-name
   ```
5. Submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [OpenWeather API](https://openweathermap.org/api) for weather data.
- [Glide](https://github.com/bumptech/glide) for image loading.
- Android and Material Design teams for design inspiration.

