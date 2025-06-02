package com.example.weathertrack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
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
    private final WeeklyWeatherAdapter adapter = new WeeklyWeatherAdapter();
    private WeatherViewModel weatherViewModel;
    private TextView CurrentTemperature, CurrentDate, CurrentHumidity, CurrentCondition, CurrentWind, CurrentUpdatedTime, WeeklyTrends;
    private ImageView CurrentDayRefresh, CurrentWeatherIcon;
    private RecyclerView recyclerViewWeather;
    private LinearLayout placeholderNoData;
    private Button buttonRefresh;
    private CardView currentWeatherCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewModel();
        setupRecyclerView();
        setupWorkManager();

        placeholderNoData = findViewById(R.id.placeholderNoData);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        currentWeatherCard = findViewById(R.id.currentWeatherCard);
        recyclerViewWeather = findViewById(R.id.recyclerViewWeather);
        WeeklyTrends = findViewById(R.id.weeklyTrends);

        buttonRefresh.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                weatherViewModel.refreshWeatherData();
            } else {
                showNoInternetDialog();
            }
        });

        observeData();
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
            if (isNetworkAvailable()) {
                CurrentDayRefresh.animate().rotation(CurrentDayRefresh.getRotation() + 360).setDuration(1000);
                weatherViewModel.refreshWeatherData();
            } else {
                updateUIForNoDataNoInternet();
                showNoInternetDialog();
            }
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
        int iconRes = R.drawable.partly_cloudy;

        switch (condition.toLowerCase()) {
            case "sunny":
                iconRes = R.drawable.sunny;
                break;
            case "partly cloudy":
                iconRes = R.drawable.partly_cloudy;
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

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("weather_sync")
                .observe(this, workInfos -> {
                    if (workInfos != null && !workInfos.isEmpty()) {
                        for (WorkInfo workInfo : workInfos) {
                            if (workInfo.getState().isFinished()) {
                                boolean noInternet = workInfo.getOutputData().getBoolean("no_internet", false);
                                if (noInternet) {
                                    showNoInternetDialog();
                                }
                            }
                        }
                    }
                });
    }

    private void observeData() {
        weatherViewModel.getLatestWeatherData().observe(this, weather -> {
            if (weather != null) {
                updateCurrentWeatherDisplay(weather);
                updateUIForDataAvailable();
            } else {
                if (!isNetworkAvailable()) {
                    updateUIForNoDataNoInternet();
                    showNoInternetDialog();
                } else {
                    weatherViewModel.refreshWeatherData();
                }
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
        Locale locale = Locale.getDefault();
        CurrentTemperature.setText(String.format(locale, "%.0f°C", weather.getTemperature()));
        Date date = new Date(weather.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Today:' MMM dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("'Last updated:' h:mm a", Locale.getDefault());
        CurrentDate.setText(dateFormat.format(date));
        CurrentHumidity.setText(String.format(locale, "%d%%", weather.getHumidity()));
        CurrentCondition.setText(weather.getCondition());
        CurrentWind.setText(String.format(locale, "%.0f km/h", weather.getWindSpeed()));
        CurrentUpdatedTime.setText(timeFormat.format(date));
        setCurrentWeatherIcon(weather.getCondition());
    }

    private void showWeatherDetails(WeatherEntity weather) {
        Locale locale = Locale.getDefault();
        new AlertDialog.Builder(this)
                .setTitle("Weather Details")
                .setMessage(String.format(locale,
                        "Date: %s\nTemperature: %.0f°C\nHumidity: %d%%\nCondition: %s\nWind Speed: %.0f km/h",
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

    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please connect to the internet to fetch the latest weather data.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void updateUIForNoDataNoInternet() {
        placeholderNoData.setVisibility(View.VISIBLE);
        currentWeatherCard.setVisibility(View.GONE);
        recyclerViewWeather.setVisibility(View.GONE);
        WeeklyTrends.setVisibility(View.GONE);
    }

    private void updateUIForDataAvailable() {
        placeholderNoData.setVisibility(View.GONE);
        currentWeatherCard.setVisibility(View.VISIBLE);
        recyclerViewWeather.setVisibility(View.VISIBLE);
        WeeklyTrends.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            android.net.NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherViewModel.cleanOldData();
    }
}