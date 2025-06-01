package com.example.weathertrack.service;

import com.example.weathertrack.model.WeatherResponse;

import java.util.Random;

public class MockWeatherApiService {
    private static final String[] CONDITIONS = {
            "Sunny", "Partly Cloudy", "Cloudy", "Snow", "Rainy", "ThunderStorm"
    };

    private static final Random random = new Random();

    public static WeatherResponse fetchWeatherData() {
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        double temperature = 15 + random.nextDouble() * 20; // 15-35°C
        int humidity = 30 + random.nextInt(50); // 30-80%
        String condition = CONDITIONS[random.nextInt(CONDITIONS.length)];
        double windSpeed = 5 + random.nextDouble() * 20; // 5-25 km/h

        return new WeatherResponse(temperature, humidity, condition, windSpeed, "Nagpur");
    }
}