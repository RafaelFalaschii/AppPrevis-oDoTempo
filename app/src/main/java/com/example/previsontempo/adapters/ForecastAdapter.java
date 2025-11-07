package com.example.previsontempo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.previsontempo.R;
import com.example.previsontempo.models.Forecast;

import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.Holder> {

    private List<Forecast> items;

    public ForecastAdapter(List<Forecast> items) {
        this.items = items;
    }

    public void updateData(List<Forecast> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Forecast f = items.get(position);

        holder.tvTemp.setText((f.getMax() != null ? f.getMax() : "") + "°C");
        holder.tvWeekday.setText(f.getWeekday() != null ? f.getWeekday() : "");

        String condition = "";
        if (f.getCondition() != null) {
            condition = f.getCondition().toLowerCase(Locale.ROOT);
        }

        if (condition.contains("rain") || condition.contains("chuva")) {
            holder.icon.setImageResource(R.drawable.ic_rainy);
        } else if (condition.contains("cloud") || condition.contains("nuvem") || condition.contains("nublado")) {
            holder.icon.setImageResource(R.drawable.ic_cloudy);
        } else if (condition.contains("fog") || condition.contains("mist") || condition.contains("nevoeiro")) {
            holder.icon.setImageResource(R.drawable.ic_foggy);
        } else if (condition.contains("clear") || condition.contains("sun") || condition.contains("ensolarado") || condition.contains("limpo")) {
            holder.icon.setImageResource(R.drawable.ic_sunny);
        } else {
            holder.icon.setImageResource(R.drawable.ic_default_weather);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvTemp, tvWeekday;
        ImageView icon;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvTemp = itemView.findViewById(R.id.itemTemp);
            tvWeekday = itemView.findViewById(R.id.itemWeekday);
            icon = itemView.findViewById(R.id.itemWeatherIcon);
        }
    }
}