package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "9ef2c0bd8c6aab6fd8b77e15a293a00e";
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView tvHL;
    private TextView windTextView;
    private TextView pressureTextView;
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = findViewById(R.id.tvTemperature);
        humidityTextView = findViewById(R.id.tvHumidity);
        tvHL = findViewById(R.id.tvHL);
        windTextView = findViewById(R.id.tvWind);
        pressureTextView = findViewById(R.id.tvPressure);

        String city = "Denver";
        fetchWeatherData(city);

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

    private void fetchWeatherData(String city) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        inputStream.close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateWeatherData(response.toString());
                            }
                        });
                    } else {
                        Log.e("WeatherApp", "HTTP request failed with status code: " + responseCode);
                    }
                } catch (IOException e) {
                    Log.e("WeatherApp", "Error fetching weather data", e);
                }
            }
        });

        thread.start();
    }

    private void updateWeatherData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject mainObject = jsonObject.optJSONObject("main");
            JSONObject windObject = jsonObject.optJSONObject("wind");
            if (mainObject != null) {
                double temperatureKelvin = mainObject.optDouble("temp");
                double temperatureCelsius = temperatureKelvin - 273.15;
                int humidity = mainObject.optInt("humidity");
                double temperatureMaxKelvin = mainObject.optDouble("temp_max");
                double temperatureMinKelvin = mainObject.optDouble("temp_min");
                double windSpeed = windObject.optDouble("speed");
                double pressure = mainObject.optDouble("pressure");
                WeatherData weatherData = new WeatherData(temperatureCelsius, humidity, temperatureMaxKelvin - 273.15, temperatureMinKelvin - 273.15, windSpeed, pressure);
                updateTextViews(weatherData);
            } else {
                Log.e("WeatherApp", "Unable to retrieve weather data. Invalid JSON structure.");
            }
        } catch (JSONException e) {
            Log.e("WeatherApp", "Error parsing weather data", e);
        }
    }

    private void updateTextViews(WeatherData weatherData) {
        int temperature = (int) weatherData.getTemperature();
        int humidity = weatherData.getHumidity();
        int highTemperature = (int) weatherData.getHighTemperature();
        int lowTemperature = (int) weatherData.getLowTemperature();
        int temperatureFahrenheit = (int) (temperature * 1.8 + 32);
        int highTemperatureFahrenheit = (int) (highTemperature * 1.8 + 32);
        int lowTemperatureFahrenheit = (int) (lowTemperature * 1.8 + 32);
        int windSpeed = (int) weatherData.getWindSpeed();
        int pressure = (int) weatherData.getPressure();
        temperatureTextView.setText(temperatureFahrenheit + "°F");
        humidityTextView.setText("Humidity: " + humidity + "%");
        String highLowTemperature = "H: " + highTemperatureFahrenheit + "°  L: " + lowTemperatureFahrenheit + "°";
        tvHL.setText(highLowTemperature);
        windTextView.setText("Wind: " + windSpeed + " km/h");
        pressureTextView.setText("Pressure: " + pressure + " hPa");
    }
}