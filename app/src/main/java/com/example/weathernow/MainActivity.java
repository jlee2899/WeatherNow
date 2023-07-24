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
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "9ef2c0bd8c6aab6fd8b77e15a293a00e";
    //New edits
    private TextView currDateTextView;
    private TextView descriptionTextView;
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

        descriptionTextView = findViewById(R.id.tvCurrWeatherType);
        currDateTextView = findViewById(R.id.tvCurrDate);
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
        String currentWeatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        String hourlyForecastApiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL currentWeatherUrl = new URL(currentWeatherApiUrl);
                    HttpURLConnection currentWeatherConnection = (HttpURLConnection) currentWeatherUrl.openConnection();
                    currentWeatherConnection.setRequestMethod("GET");

                    int currentWeatherResponseCode = currentWeatherConnection.getResponseCode();
                    if (currentWeatherResponseCode == HttpURLConnection.HTTP_OK) {
                        InputStream currentWeatherInputStream = currentWeatherConnection.getInputStream();
                        BufferedReader currentWeatherReader = new BufferedReader(new InputStreamReader(currentWeatherInputStream));
                        StringBuilder currentWeatherResponse = new StringBuilder();
                        String currentWeatherLine;
                        while ((currentWeatherLine = currentWeatherReader.readLine()) != null) {
                            currentWeatherResponse.append(currentWeatherLine);
                        }
                        currentWeatherReader.close();
                        currentWeatherInputStream.close();

                        updateCurrentWeatherData(currentWeatherResponse.toString());
                    } else {
                        Log.e("WeatherApp", "Current weather data request failed with status code: " + currentWeatherResponseCode);
                    }

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

    private void updateCurrentWeatherData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject mainObject = jsonObject.optJSONObject("main");
            JSONObject windObject = jsonObject.optJSONObject("wind");
            //New edit
            JSONArray weatherArray = jsonObject.optJSONArray("weather");
            if (mainObject != null && weatherArray != null && weatherArray.length() > 0) {
                double temperatureKelvin = mainObject.optDouble("temp");
                double temperatureCelsius = temperatureKelvin - 273.15;
                int humidity = mainObject.optInt("humidity");
                double temperatureMaxKelvin = mainObject.optDouble("temp_max");
                double temperatureMinKelvin = mainObject.optDouble("temp_min");
                double windSpeed = windObject.optDouble("speed");
                double pressure = mainObject.optDouble("pressure");
                //new edit
                String description = weatherArray.getJSONObject(0).optString("description");
                WeatherData weatherData = new WeatherData(temperatureCelsius, humidity, temperatureMaxKelvin - 273.15, temperatureMinKelvin - 273.15, windSpeed, pressure, description);
                updateTextViews(weatherData);
            } else {
                Log.e("WeatherApp", "Unable to retrieve weather data. Invalid JSON structure.");
            }
        } catch (JSONException e) {
            Log.e("WeatherApp", "Error parsing weather data", e);
        }
    }


    private void updateHourlyForecast(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray hourlyForecastArray = jsonObject.optJSONArray("list");

            if (hourlyForecastArray == null) {
                Log.e("WeatherApp", "Unable to retrieve hourly forecast data. Invalid JSON structure.");
                return;
            }

            ArrayList<HourlyForecast> items = new ArrayList<>();
            int forecastCount = 0;

            for (int i = 0; i < hourlyForecastArray.length(); i++) {
                JSONObject forecastObject = hourlyForecastArray.getJSONObject(i);
                long timestamp = forecastObject.optLong("dt");
                String time = convertTimestampToHour(timestamp);
                JSONObject mainObject = forecastObject.optJSONObject("main");
                double temperatureKelvin = mainObject.optDouble("temp");
                double temperatureCelsius = temperatureKelvin - 273.15;
                int temperatureFahrenheit = (int) (temperatureCelsius * 9 / 5) + 32;
                JSONArray weatherArray = forecastObject.optJSONArray("weather");
                String weatherIcon = "unknown";

                if (weatherArray != null && weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    weatherIcon = weatherObject.optString("icon");
                }

                items.add(new HourlyForecast(time, temperatureFahrenheit, weatherIcon));
                forecastCount++;
                if (forecastCount >= 9) {
                    break;
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapterHourly = new HourlyAdapter(items);
                    recyclerView.setAdapter(adapterHourly);
                }
            });

        } catch (JSONException e) {
            Log.e("WeatherApp", "Error parsing hourly forecast data", e);
        }
    }


    private String convertTimestampToHour(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(date);
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
        //New edit
        String description = weatherData.getDescription();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //New edit
                descriptionTextView.setText(description);
                temperatureTextView.setText(temperatureFahrenheit + "°F");
                humidityTextView.setText("Humidity: \n" + humidity + "%");
                String highLowTemperature = "H: " + highTemperatureFahrenheit + "°  L: " + lowTemperatureFahrenheit + "°";
                tvHL.setText(highLowTemperature);
                windTextView.setText("Wind: \n" + windSpeed + " km/h");
                pressureTextView.setText("Pressure: \n" + pressure + " hPa");
            }
        });
    }
}