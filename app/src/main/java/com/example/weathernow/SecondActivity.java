package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        ImageView backBtn = findViewById(R.id.leftBtn);
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