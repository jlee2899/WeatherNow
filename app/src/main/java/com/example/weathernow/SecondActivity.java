package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTomorrow;
    private RecyclerView recyclerView;
    private static final String API_KEY = "APIK";

    private TextView myCityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tmmrRecyclerView();
        backToMain();

        String city = "Denver";
        fetchFiveDayWeatherData(city);

        myCityTextView = findViewById(R.id.tvCityName);
        myCityTextView.setText(city);
    }

    private void backToMain() {
        ImageView backBtn = findViewById(R.id.leftBtn);
        backBtn.setOnClickListener(view -> startActivity(new Intent(SecondActivity.this, MainActivity.class)));
    }

    private void tmmrRecyclerView() {
        ArrayList<TomorrowDomain> items = new ArrayList<>();

        recyclerView = findViewById(R.id.rv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTomorrow = new TomorrowAdapter(items);
        recyclerView.setAdapter(adapterTomorrow);
    }

    private void fetchFiveDayWeatherData(String city) {
        String hourlyForecastApiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL hourlyForecastUrl = new URL(hourlyForecastApiUrl);
                    HttpURLConnection hourlyForecastConnection = (HttpURLConnection) hourlyForecastUrl.openConnection();
                    hourlyForecastConnection.setRequestMethod("GET");

                    int hourlyForecastResponseCode = hourlyForecastConnection.getResponseCode();
                    if (hourlyForecastResponseCode == HttpURLConnection.HTTP_OK) {
                        InputStream hourlyForecastInputStream = hourlyForecastConnection.getInputStream();
                        BufferedReader hourlyForecastReader = new BufferedReader(new InputStreamReader(hourlyForecastInputStream));
                        StringBuilder hourlyForecastResponse = new StringBuilder();
                        String hourlyForecastLine;
                        while ((hourlyForecastLine = hourlyForecastReader.readLine()) != null) {
                            hourlyForecastResponse.append(hourlyForecastLine);
                        }
                        hourlyForecastReader.close();
                        hourlyForecastInputStream.close();

                        updateHourlyForecast(hourlyForecastResponse.toString());
                    } else {
                        Log.e("WeatherApp", "Hourly forecast data request failed with status code: " + hourlyForecastResponseCode);
                    }

                } catch (IOException e) {
                    Log.e("WeatherApp", "Error fetching weather data", e);
                }
            }
        });

        thread.start();
    }

    private void updateHourlyForecast(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray fiveDayForecastArray = jsonObject.optJSONArray("list");

            if (fiveDayForecastArray == null) {
                Log.e("WeatherApp", "Unable to retrieve hourly forecast data. Invalid JSON structure.");
                return;
            }

            ArrayList<TomorrowDomain> items = new ArrayList<>();

            for (int i = 0; i < fiveDayForecastArray.length(); i += 8) {
                JSONObject forecastObject = fiveDayForecastArray.getJSONObject(i);
                long timestamp = forecastObject.optLong("dt");
                String time = convertTimestampToHour(timestamp);
                JSONObject mainObject = forecastObject.optJSONObject("main");
                double temperatureKelvin = mainObject.optDouble("temp");
                double temperatureCelsius = temperatureKelvin - 273.15;
                int temperatureFahrenheit = (int) (temperatureCelsius * 9 / 5) + 32;
                JSONArray weatherArray = forecastObject.optJSONArray("weather");
                String weatherIconCode = "unknown";

                if (weatherArray != null && weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    weatherIconCode = weatherObject.optString("icon");
                }

                String weatherIcon = getWeatherIconUrl(weatherIconCode);

                items.add(new TomorrowDomain(time, weatherIcon, temperatureFahrenheit));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapterTomorrow = new TomorrowAdapter(items);
                    recyclerView.setAdapter(adapterTomorrow);
                }
            });

        } catch (JSONException e) {
            Log.e("WeatherApp", "Error parsing hourly forecast data", e);
        }
    }

    private String convertTimestampToHour(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        return sdf.format(date);
    }

    private String getWeatherIconUrl(String iconCode) {
        return "https://openweathermap.org/img/wn/" + iconCode + ".png";
    }
}