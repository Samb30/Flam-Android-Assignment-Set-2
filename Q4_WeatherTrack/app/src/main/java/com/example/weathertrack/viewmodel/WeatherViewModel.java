package com.example.weathertrack.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weathertrack.database.WeatherEntity;
import com.example.weathertrack.repository.WeatherRepository;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository repository;
    private final LiveData<List<WeatherEntity>> allWeatherData;
    private final LiveData<WeatherEntity> latestWeatherData;
    private final LiveData<List<WeatherEntity>> weeklyWeatherData;
    private final MutableLiveData<String> statusMessage;
    private final MutableLiveData<Boolean> isLoading;

    public WeatherViewModel(Application application) {
        super(application);
        repository = new WeatherRepository(application);
        allWeatherData = repository.getAllWeatherData();
        latestWeatherData = repository.getLatestWeatherData();
        weeklyWeatherData = repository.getWeeklyWeatherData();
        statusMessage = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
    }

    public LiveData<List<WeatherEntity>> getAllWeatherData() {
        return allWeatherData;
    }

    public LiveData<WeatherEntity> getLatestWeatherData() {
        return latestWeatherData;
    }

    public LiveData<List<WeatherEntity>> getWeeklyWeatherData() {
        return weeklyWeatherData;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void refreshWeatherData() {
        isLoading.setValue(true);
        repository.fetchAndSaveWeatherData(new WeatherRepository.WeatherFetchCallback() {
            @Override
            public void onSuccess(String message) {
                isLoading.postValue(false);
                statusMessage.postValue(message);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                statusMessage.postValue(error);
            }
        });
    }

    public void cleanOldData() {
        repository.cleanOldData();
    }
}