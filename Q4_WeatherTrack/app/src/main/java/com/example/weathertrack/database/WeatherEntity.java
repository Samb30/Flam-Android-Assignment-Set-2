package com.example.weathertrack.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_data")
public class WeatherEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "temperature")
    private double temperature;

    @ColumnInfo(name = "humidity")
    private int humidity;

    @ColumnInfo(name = "condition")
    private String condition;

    @ColumnInfo(name = "wind_speed")
    private double windSpeed;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "date_string")
    private String dateString;

    public WeatherEntity() {
    }

    public WeatherEntity(double temperature, int humidity, String condition,
                         double windSpeed, long timestamp, String dateString) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
        this.dateString = dateString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}