package com.example.weathertrack.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather_data ORDER BY timestamp DESC")
    LiveData<List<WeatherEntity>> getAllWeatherData();

    @Query("SELECT * FROM weather_data ORDER BY timestamp DESC LIMIT 1")
    LiveData<WeatherEntity> getLatestWeatherData();

    @Query("SELECT * FROM weather_data WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    LiveData<List<WeatherEntity>> getWeatherDataFromTime(long startTime);

    @Query("SELECT * FROM weather_data WHERE date_string = :dateString LIMIT 1")
    WeatherEntity getWeatherDataByDate(String dateString);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherData(WeatherEntity weatherEntity);

    @Query("DELETE FROM weather_data WHERE timestamp < :cutoffTime")
    void deleteOldData(long cutoffTime);

    @Update
    void updateWeatherData(WeatherEntity weatherEntity);
}