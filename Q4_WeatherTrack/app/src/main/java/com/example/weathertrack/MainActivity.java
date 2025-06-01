package com.example.weathertrack;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.example.weathertrack.adapter.WeeklyWeatherAdapter;
import com.example.weathertrack.database.WeatherEntity;
import com.example.weathertrack.viewmodel.WeatherViewModel;
import com.example.weathertrack.worker.WeatherSyncWorker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private WeatherViewModel weatherViewModel;
    private TextView CurrentTemperature, CurrentDate, CurrentHumidity, CurrentCondition, CurrentWind, CurrentUpdatedTime;
    private ImageView CurrentDayRefresh, CurrentWeatherIcon;
    private RecyclerView recyclerViewWeather;
    private final WeeklyWeatherAdapter adapter = new WeeklyWeatherAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupWorkManager();
        observeData();

        weatherViewModel.getLatestWeatherData().observe(this, weather -> {
            if (weather != null) {
                updateCurrentWeatherDisplay(weather);
                long now = System.currentTimeMillis();
                if (now - weather.getTimestamp() > 6 * 60 * 60 * 1000) {
                    weatherViewModel.refreshWeatherData();
                }
            } else {
                weatherViewModel.refreshWeatherData();
            }
        });
    }

    private void initViews() {
        CurrentTemperature = findViewById(R.id.CurrentTemp);
        CurrentDate = findViewById(R.id.CurrentDate);
        CurrentHumidity = findViewById(R.id.CurrentHumidityStats);
        CurrentCondition = findViewById(R.id.CurrentConditionStats);
        CurrentWind = findViewById(R.id.CurrentWindStats);
        CurrentUpdatedTime = findViewById(R.id.UpdatedTime);
        CurrentDayRefresh = findViewById(R.id.CurrentRefresh);
        recyclerViewWeather = findViewById(R.id.recyclerViewWeather);
        CurrentWeatherIcon = findViewById(R.id.CurrentWeather);

        CurrentDayRefresh.setOnClickListener(v -> {
            CurrentDayRefresh.animate().rotation(CurrentDayRefresh.getRotation() + 360).setDuration(1000);
            weatherViewModel.refreshWeatherData();
        });
    }

    private void setupViewModel() {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
    }

    private void setupRecyclerView() {
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWeather.setAdapter(adapter);
        adapter.setOnItemClickListener(this::showWeatherDetails);
    }

    private void setCurrentWeatherIcon(String condition) {
        int iconRes = R.drawable.partly_cloudy_svg;

        switch (condition.toLowerCase()) {
            case "sunny":
                iconRes = R.drawable.sunny;
                break;
            case "partly cloudy":
                iconRes = R.drawable.partly_cloudy_svg;
                break;
            case "cloudy":
                iconRes = R.drawable.cloudy;
                break;
            case "snow":
                iconRes = R.drawable.snow;
                break;
            case "rainy":
                iconRes = R.drawable.rain;
                break;
            case "thunderstorm":
                iconRes = R.drawable.thunder;
                break;
        }
        CurrentWeatherIcon.setImageResource(iconRes);
    }

    private void setupWorkManager() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest weatherSyncRequest = new PeriodicWorkRequest.Builder(
                WeatherSyncWorker.class, 6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "weather_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                weatherSyncRequest);
    }

    private void observeData() {
        weatherViewModel.getLatestWeatherData().observe(this, weather -> {
            if (weather != null) {
                updateCurrentWeatherDisplay(weather);
            }
        });

        weatherViewModel.getWeeklyWeatherData().observe(this, weatherList -> {
            if (weatherList != null) {
                adapter.setWeatherList(weatherList);
            }
        });

        weatherViewModel.getStatusMessage().observe(this, message -> {
            if (message != null) {
                showToast(message);
            }
        });

        weatherViewModel.getIsLoading().observe(this, isLoading -> {
        });
    }

    private void updateCurrentWeatherDisplay(WeatherEntity weather) {
        CurrentTemperature.setText(String.format("%.0f°C", weather.getTemperature()));
        Date date = new Date(weather.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Today:' MMM dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("'Last updated:' h:mm a", Locale.getDefault());
        CurrentDate.setText(dateFormat.format(date));
        CurrentHumidity.setText(String.format("%d%%", weather.getHumidity()));
        CurrentCondition.setText(weather.getCondition());
        CurrentWind.setText(String.format("%.0f km/h", weather.getWindSpeed()));
        CurrentUpdatedTime.setText(timeFormat.format(date));
        setCurrentWeatherIcon(weather.getCondition());
    }

    private void showWeatherDetails(WeatherEntity weather) {
        new AlertDialog.Builder(this)
                .setTitle("Weather Details")
                .setMessage(String.format(
                        "Date: %s\nTemperature: %.1f°C\nHumidity: %d%%\nCondition: %s\nWind Speed: %.1f km/h",
                        new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault()).format(new Date(weather.getTimestamp())),
                        weather.getTemperature(),
                        weather.getHumidity(),
                        weather.getCondition(),
                        weather.getWindSpeed()
                ))
                .setPositiveButton("OK", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherViewModel.cleanOldData();
    }
}