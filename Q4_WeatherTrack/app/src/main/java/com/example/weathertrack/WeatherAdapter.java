package com.example.weathertrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private final List<WeatherModel> weatherList;
    private final Context context;

    public WeatherAdapter(Context context, List<WeatherModel> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_day, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherModel model = weatherList.get(position);

        holder.tvDay.setText(model.day);
        holder.tvTempSummary.setText(model.tempSummary);
        holder.tvDetailHumidity.setText("Humidity: " + model.humidity);
        holder.tvDetailCondition.setText("Condition: " + model.condition);
        holder.tvDetailTime.setText("Last updated: " + model.lastUpdated);
        holder.ivIcon.setImageResource(model.iconResId);

        // Toggle expandable layout
        holder.itemView.setOnClickListener(v -> {
            if (holder.expandedLayout.getVisibility() == View.GONE) {
                fadeIn(holder.expandedLayout);
                holder.expandedLayout.setVisibility(View.VISIBLE);
            } else {
                holder.expandedLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTempSummary, tvDetailHumidity, tvDetailCondition, tvDetailTime;
        ImageView ivIcon;
        LinearLayout expandedLayout;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTempSummary = itemView.findViewById(R.id.tvTempSummary);
            tvDetailHumidity = itemView.findViewById(R.id.tvDetailHumidity);
            tvDetailCondition = itemView.findViewById(R.id.tvDetailCondition);
            tvDetailTime = itemView.findViewById(R.id.tvDetailTime);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            expandedLayout = itemView.findViewById(R.id.expandedLayout);
        }
    }

    private void fadeIn(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
