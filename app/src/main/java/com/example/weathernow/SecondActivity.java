package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTomorrow;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //initRecyclerView();
        backToMain();
    }

    private void backToMain() {
        TextView backBtn = findViewById(R.id.tvBackToMain);
        backBtn.setOnClickListener(view -> startActivity(new Intent(SecondActivity.this, MainActivity.class)));
    }

    private void initRecyclerView() {
        ArrayList<TomorrowDomain> items = new ArrayList<>();

        items.add(new TomorrowDomain("Saturday", "cloudy", 50,  30));
        items.add(new TomorrowDomain("Sunday", "cloudy", 50, 30));
        items.add(new TomorrowDomain("Monday", "cloudy", 50, 30));
        items.add(new TomorrowDomain("Tuesday", "cloudy", 50, 30));
        items.add(new TomorrowDomain("Wednesday", "cloudy", 50, 30));
        items.add(new TomorrowDomain("Thursday", "cloudy", 50, 30));

        recyclerView = findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTomorrow = new TomorrowAdapter(items);
        recyclerView.setAdapter(adapterTomorrow);
    }
}