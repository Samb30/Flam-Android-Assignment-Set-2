package com.example.weathertrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weathertrack.R;
import com.example.weathertrack.database.WeatherEntity;
import org.jspecify.annotations.NonNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyWeatherAdapter extends RecyclerView.Adapter<WeeklyWeatherAdapter.WeatherViewHolder>{
    private List<WeatherEntity> weatherList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WeatherEntity weather);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setWeatherList(List<WeatherEntity> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_day, parent, false); // Your second layout file
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherEntity weather = weatherList.get(position);
        holder.bind(weather);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView Day, SummaryTemperature, SummaryHumidity, SummaryCondition, SummaryWind, SummaryUpdatedTime;
        private ImageView SummaryWeather, SummaryExpand;
        private LinearLayout expandedLayout;
        private boolean isExpanded = false;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            Day = itemView.findViewById(R.id.SummaryDay);
            SummaryTemperature = itemView.findViewById(R.id.SummaryTemperature);
            SummaryHumidity = itemView.findViewById(R.id.HumidityInfo);
            SummaryCondition = itemView.findViewById(R.id.ConditionInfo);
            SummaryWind = itemView.findViewById(R.id.WindInfo);
            SummaryUpdatedTime = itemView.findViewById(R.id.TimeInfo);
            SummaryWeather = itemView.findViewById(R.id.SummaryWeatherIcon);
            SummaryExpand = itemView.findViewById(R.id.SummaryExpandIcon);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);

            itemView.setOnClickListener(v -> toggleExpanded());
            SummaryExpand.setOnClickListener(v -> toggleExpanded());
        }

        public void bind(WeatherEntity weather) {
            Date date = new Date(weather.getTimestamp());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

            Day.setText(dayFormat.format(date));
            SummaryTemperature.setText(String.format("Temp: %.1f°C", weather.getTemperature()));
            SummaryHumidity.setText(String.format("Humidity: %d%%", weather.getHumidity()));
            SummaryCondition.setText(String.format("Condition: %s", weather.getCondition()));
            SummaryWind.setText(String.format("Wind: %.1f km/h", weather.getWindSpeed()));
            SummaryUpdatedTime.setText(String.format("Last updated: %s", timeFormat.format(date)));

            setWeatherIcon(weather.getCondition());

            isExpanded = false;
            expandedLayout.setVisibility(View.GONE);
            SummaryExpand.setRotation(0);
        }

        private void setWeatherIcon(String condition) {
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
                case "stormy":
                    iconRes = R.drawable.thunder;
                    break;
            }

            SummaryWeather.setImageResource(iconRes);
        }

        private void toggleExpanded() {
            isExpanded = !isExpanded;

            if (isExpanded) {
                expandedLayout.setVisibility(View.VISIBLE);
                SummaryExpand.animate().rotation(180).setDuration(300);
                if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(weatherList.get(position));
                }
            }
            } else {
                expandedLayout.setVisibility(View.GONE);
                SummaryExpand.animate().rotation(0).setDuration(300);
            }
        }
    }
}
