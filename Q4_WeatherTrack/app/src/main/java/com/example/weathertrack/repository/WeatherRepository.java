package com.example.weathertrack.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.weathertrack.database.WeatherDao;
import com.example.weathertrack.database.WeatherDatabase;
import com.example.weathertrack.database.WeatherEntity;
import com.example.weathertrack.model.WeatherResponse;
import com.example.weathertrack.service.MockWeatherApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherRepository {
    private final WeatherDao weatherDao;
    private final LiveData<List<WeatherEntity>> allWeatherData;
    private final LiveData<WeatherEntity> latestWeatherData;
    private final ExecutorService executor;

    public WeatherRepository(Application application) {
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
        allWeatherData = weatherDao.getAllWeatherData();
        latestWeatherData = weatherDao.getLatestWeatherData();
        executor = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<WeatherEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    public LiveData<WeatherEntity> getLatestWeatherData() {
        return latestWeatherData;
    }

    public LiveData<List<WeatherEntity>> getWeeklyWeatherData() {
        long weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);
        return weatherDao.getWeatherDataFromTime(weekAgo);
    }

    public void fetchAndSaveWeatherData(WeatherFetchCallback callback) {
        executor.execute(() -> {
            try {
                if (MockWeatherApiService.shouldSimulateFailure()) {
                    callback.onError("Unable to retrieve weather data. Please try again later.");
                    return;
                }

                WeatherResponse response = MockWeatherApiService.fetchWeatherData();

                String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(new Date());

                WeatherEntity existing = weatherDao.getWeatherDataByDate(dateString);

                WeatherEntity entity = new WeatherEntity(
                        response.getTemperature(),
                        response.getHumidity(),
                        response.getCondition(),
                        response.getWindSpeed(),
                        System.currentTimeMillis(),
                        dateString
                );

                if (existing != null) {
                    entity.setId(existing.getId());
                    weatherDao.updateWeatherData(entity);
                } else {
                    weatherDao.insertWeatherData(entity);
                }

                callback.onSuccess("Weather details updated successfully");

            } catch (Exception e) {
                callback.onError("Error: " + e.getMessage());
            }
        });
    }

    public void cleanOldData() {
        executor.execute(() -> {
            long cutoffTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
            weatherDao.deleteOldData(cutoffTime);
        });
    }

    public interface WeatherFetchCallback {
        void onSuccess(String message);

        void onError(String error);
    }
}
