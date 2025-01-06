package com.example.weatherforecast.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherData;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private Context context;
    private List<WeatherData> forecastList;

    public HourlyForecastAdapter(Context context, List<WeatherData> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hourly_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData forecast = forecastList.get(position);
        holder.hourMinMax.setText(forecast.getTempMin()+"/"+forecast.getTempMax());
        holder.hourCelsius.setText(forecast.getTemperature()+"°C");
        holder.hourDateTime.setText(forecast.getDateTime());

        // Load weather icon using Glide
        Glide.with(context).load(forecast.getIcon()).into(holder.hourlyIcon);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourDateTime, hourMinMax,hourCelsius;
        ImageView hourlyIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourDateTime = itemView.findViewById(R.id.hourDateTime);
            hourMinMax = itemView.findViewById(R.id.hourMinMax);
            hourCelsius = itemView.findViewById(R.id.hourCelsius);
            hourlyIcon = itemView.findViewById(R.id.hourlyIcon);
        }
    }
}
