package com.example.weathertrack.worker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weathertrack.repository.WeatherRepository;

import org.jspecify.annotations.NonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WeatherSyncWorker extends Worker {
    private static final String TAG = "WeatherSyncWorker";

    public WeatherSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Weather sync worker started.");

        try {
            WeatherRepository repository = new WeatherRepository((Application) getApplicationContext());

            if (!isNetworkAvailable()) {
                Log.w(TAG, "No network available, please try again later.");

                Data output = new Data.Builder()
                        .putBoolean("no_internet", true)
                        .build();

                return Result.failure(output);
            }

            CountDownLatch latch = new CountDownLatch(1);
            final Result[] result = {Result.success()};

            repository.fetchAndSaveWeatherData(new WeatherRepository.WeatherFetchCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "Weather sync successful: " + message);
                    result[0] = Result.success();
                    latch.countDown();
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Weather sync failed: " + error);
                    result[0] = Result.retry();
                    latch.countDown();
                }
            });

            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                Log.e(TAG, "Weather sync timed out");
                return Result.retry();
            }
            return result[0];

        } catch (Exception e) {
            Log.e(TAG, "Weather sync worker error.", e);
            return Result.failure();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }else {
            android.net.NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
    }
}