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
        tmmrRecyclerView();
        backToMain();
    }

    private void backToMain() {
        TextView backBtn = findViewById(R.id.tvBackToMain);
        backBtn.setOnClickListener(view -> startActivity(new Intent(SecondActivity.this, MainActivity.class)));
    }

    private void tmmrRecyclerView() {
        ArrayList<TomorrowDomain> items = new ArrayList<>();

        items.add(new TomorrowDomain("Saturday", "cloudy", "Hi ", "Lo"));
        items.add(new TomorrowDomain("Sunday", "cloudy", "Hi ", "Lo"));
        items.add(new TomorrowDomain("Monday", "cloudy", "Hi ", "Lo"));
        items.add(new TomorrowDomain("Tuesday", "cloudy", "Hi ", "Lo"));
        items.add(new TomorrowDomain("Wednesday", "cloudy", "Hi ", "Lo"));
        items.add(new TomorrowDomain("Thursday", "cloudy", "Hi ", "Lo"));

        recyclerView = findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTomorrow = new TomorrowAdapter(items);
        recyclerView.setAdapter(adapterTomorrow);
    }
}