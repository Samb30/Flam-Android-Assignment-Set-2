package com.example.weathertrack.model;

public class WeatherResponse {
    private double temperature;
    private int humidity;
    private String condition;
    private double windSpeed;
    private String location;
    private long timestamp;

    public WeatherResponse() {}

    public WeatherResponse(double temperature, int humidity, String condition,
                           double windSpeed, String location) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
    }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}