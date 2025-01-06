package com.example.weatherforecast;

public class WeatherData {
    private String weatherDescription;
    private double temperature;
    private double feelLike;
    private double tempMin;
    private double tempMax;
    private String icon;
    private String dateTime;


    public WeatherData( String weatherDescription, double temperature, String icon,double feelLike,double tempMin, double tempMax) {
        this.weatherDescription = weatherDescription;
        this.temperature = temperature;
        this.feelLike=feelLike;
        this.tempMin=tempMin;
        this.tempMax=tempMax;
        this.icon=icon;
    }
    // Constructor without weatherDescription
    public WeatherData(String dateTime, double temperature, double feelLike, double tempMin, double tempMax, String icon) {
        this.weatherDescription = "No description available"; // Default value
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.feelLike = feelLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.icon = icon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public double getFeelLike() {
        return feelLike;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTemperature() {
        return temperature;
    }
    public String getIcon() {
        return icon;
    }
}
