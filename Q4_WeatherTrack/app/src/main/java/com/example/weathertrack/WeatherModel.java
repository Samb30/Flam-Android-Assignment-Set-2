package com.example.weathertrack;

public class WeatherModel {
    public String day;
    public String tempSummary;
    public String humidity;
    public String condition;
    public String lastUpdated;
    public int iconResId;

    public WeatherModel(String day, String tempSummary, String humidity, String condition, String lastUpdated, int iconResId) {
        this.day = day;
        this.tempSummary = tempSummary;
        this.humidity = humidity;
        this.condition = condition;
        this.lastUpdated = lastUpdated;
        this.iconResId = iconResId;
    }
}
