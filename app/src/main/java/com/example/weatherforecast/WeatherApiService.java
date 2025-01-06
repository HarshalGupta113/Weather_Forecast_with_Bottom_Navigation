package com.example.weatherforecast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApiService {
    private static final String API_KEY = "db170164242cd1f9b0959569ab92bdb0"; // Replace with your OpenWeather API key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String BASE_URL_HOUR = "https://api.openweathermap.org/data/2.5/forecast";

    public interface WeatherDataCallback {
        void onSuccess(WeatherData weatherData);
        void onHourlySuccess(List<WeatherData> hourlyForecasts);
        void onFailure(String errorMessage);
    }

    public static void fetchWeatherData(double latitude, double longitude, WeatherDataCallback callback) {
        String url = BASE_URL + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed to fetch weather data: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);

                        // Parse weather data\
                        String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                        String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
                        double feelLike = jsonObject.getJSONObject("main").getDouble("feels_like");
                        double tempMin = jsonObject.getJSONObject("main").getDouble("temp_min");
                        double tempMax = jsonObject.getJSONObject("main").getDouble("temp_max");

                        // Pass parsed data to callback
                        callback.onSuccess(new WeatherData(description, temperature,icon,feelLike,tempMin,tempMax));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Error parsing weather data.");
                    }
                } else {
                    callback.onFailure("Error: " + response.message());
                }
            }
        });
    }

    public static void fetchHourlyData(double latitude, double longitude, WeatherDataCallback callback) {
        String url = BASE_URL_HOUR + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Failed to fetch hourly data.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);

                        // Parse hourly data
                        List<WeatherData> hourlyForecasts = new ArrayList<>();
                        JSONArray hourlyArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < hourlyArray.length(); i++) {
                            JSONObject hourObject = hourlyArray.getJSONObject(i);
                            String dateTime = hourObject.getString("dt_txt");
                            double temperature = hourObject.getJSONObject("main").getDouble("temp");
                            double feelsLike = hourObject.getJSONObject("main").getDouble("feels_like");
                            double tempMin = hourObject.getJSONObject("main").getDouble("temp_min");
                            double tempMax = hourObject.getJSONObject("main").getDouble("temp_max");
                            String icon = hourObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                            hourlyForecasts.add(new WeatherData(dateTime, temperature, feelsLike, tempMin, tempMax, iconUrl));
                        }

                        // Pass parsed data to callback
                        callback.onHourlySuccess(hourlyForecasts);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Error parsing hourly data.");
                    }
                } else {
                    callback.onFailure("Error: " + response.message());
                }
            }
        });
    }

}
