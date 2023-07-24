package com.example.weathernow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    ArrayList <HourlyForecast> items;
    Context context;

    public HourlyAdapter(ArrayList<HourlyForecast> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.hour.setText(items.get(position).getHour());
        holder.temp.setText(items.get(position).getTemp() + "°");

        Glide.with(context)
                .load(items.get(position).getPath()) // Use the weather icon URL from HourlyForecast
                .into(holder.path);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hour, temp;
        ImageView path;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.tvHourlyTime);
            temp = itemView.findViewById(R.id.tvHourlyTemp);
            path = itemView.findViewById(R.id.ivHourlyPic);
        }
    }
}
