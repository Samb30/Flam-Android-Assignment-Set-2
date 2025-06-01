package com.example.weathertrack.worker;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
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
                Log.w(TAG, "No network available, sync failed.");
                return Result.retry();
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

            latch.await(30, TimeUnit.SECONDS);
            return result[0];

        } catch (Exception e) {
            Log.e(TAG, "Weather sync worker error.", e);
            return Result.failure();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}