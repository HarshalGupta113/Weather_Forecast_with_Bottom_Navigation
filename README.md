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

## Screenshots
### Dark Mode
<img src="https://github.com/user-attachments/assets/8b308891-4b27-49c3-9ea1-766cde4dbe29" alt="Screenshot_20250106_191136 (Phone)" width="300">
<img src="https://github.com/user-attachments/assets/2e3d9a30-5d56-48d0-b685-233b919ade0d" alt="Screenshot_20250106_191208" width="300">
<img src="https://github.com/user-attachments/assets/f0c7327a-e12b-43ce-9f74-66071c6a21b0" alt="Screenshot_20250106_191229" width="300">


## Acknowledgements

- [OpenWeather API](https://openweathermap.org/api) for weather data.
- [Glide](https://github.com/bumptech/glide) for image loading.
- Android and Material Design teams for design inspiration.

