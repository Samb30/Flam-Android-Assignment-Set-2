package com.example.weathertrack.service;

import com.example.weathertrack.model.WeatherResponse;

import java.util.Calendar;
import java.util.Random;

public class MockWeatherApiService {
    private static final String[] ALL_CONDITIONS = {
            "Sunny", "Partly Cloudy", "Cloudy", "Rainy", "ThunderStorm"
    };

    private static final String[] NIGHT_CONDITIONS = {
            "Cloudy", "Rainy", "ThunderStorm"
    };

    private static final Random random = new Random();

    public static WeatherResponse fetchWeatherData() {
        try {
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        double temperature = 0 + random.nextDouble() * 35;
        int humidity = 30 + random.nextInt(50);
        double windSpeed = 5 + random.nextDouble() * 20;

        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String condition;
        if (temperature < 8) {
            condition = "Snow";
        } else {
            String[] validConditions = (hourOfDay >= 18) ? NIGHT_CONDITIONS : ALL_CONDITIONS;
            condition = validConditions[random.nextInt(validConditions.length)];
        }

        return new WeatherResponse(temperature, humidity, condition, windSpeed, "Nagpur");
    }

    public static boolean shouldSimulateFailure() {
        return random.nextInt(10) == 0;
    }
}