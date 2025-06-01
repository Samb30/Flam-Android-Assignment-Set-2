package com.example.weathertrack;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WeatherAdapter adapter;
    List<WeatherModel> weatherList;
    private ImageView refreshIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this matches your layout name

        recyclerView = findViewById(R.id.recyclerViewWeather);

        weatherList = new ArrayList<>();
        weatherList.add(new WeatherModel("Monday, May 27", "Temp: 22°C - 28°C", "60%", "Sunny", "6:00 AM", R.drawable.cloudy_day_svg));
        weatherList.add(new WeatherModel("Tuesday, May 28", "Temp: 21°C - 26°C", "55%", "Rainy", "6:00 AM", R.drawable.cloudy_day_svg));
        // Add more...
        weatherList.add(new WeatherModel("Monday, May 27", "Temp: 22°C - 28°C", "60%", "Sunny", "6:00 AM", R.drawable.cloudy_day_svg));
        weatherList.add(new WeatherModel("Tuesday, May 28", "Temp: 21°C - 26°C", "55%", "Rainy", "6:00 AM", R.drawable.cloudy_day_svg));
        weatherList.add(new WeatherModel("Monday, May 27", "Temp: 22°C - 28°C", "60%", "Sunny", "6:00 AM", R.drawable.cloudy_day_svg));
        weatherList.add(new WeatherModel("Tuesday, May 28", "Temp: 21°C - 26°C", "55%", "Rainy", "6:00 AM", R.drawable.cloudy_day_svg));
        weatherList.add(new WeatherModel("Monday, May 27", "Temp: 22°C - 28°C", "60%", "Sunny", "6:00 AM", R.drawable.cloudy_day_svg));

        adapter = new WeatherAdapter(this, weatherList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Hook refresh icon
        refreshIcon = findViewById(R.id.ivRefresh);

        // Handle refresh click
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simulate refresh
                Toast.makeText(MainActivity.this, "Refreshing weather data...", Toast.LENGTH_SHORT).show();

                // TODO: Trigger fetch from API or local mock data
            }
        });
    }
}
