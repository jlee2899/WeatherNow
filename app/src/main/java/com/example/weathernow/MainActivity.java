package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        to7Days();
    }

    private void to7Days() {
        TextView next7DaysBtn = findViewById(R.id.tv7days);
        next7DaysBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SecondActivity.class)));
    }

    private void initRecyclerView() {
        ArrayList<HourlyForecast> items = new ArrayList<>();

        items.add(new HourlyForecast("10 pm",19, "cloudy"));
        items.add(new HourlyForecast("11 pm",20, "sun"));
        items.add(new HourlyForecast("12 pm",21, "wind"));
        items.add(new HourlyForecast("1 am",20, "rainy"));
        items.add(new HourlyForecast("2 am",22, "storm"));

        recyclerView = findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly = new HourlyAdapter(items);
        recyclerView.setAdapter(adapterHourly);
    }
}