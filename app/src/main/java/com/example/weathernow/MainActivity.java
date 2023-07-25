package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private TextInputEditText textInputEditText;

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

        String defaultCity = "Denver";
        fetchWeatherData(defaultCity);

        initRecyclerView();
        to7Days();
        toSavedList();

        //New edits
        textInputEditText = findViewById(R.id.textInputEditText);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);

        // Set up the end icon click listener for the search icon
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Called when the search icon is clicked
                // Get the city entered by the user in the TextInputEditText
                String city = textInputEditText.getText().toString();

                //Fetching weather data for the searched city
                fetchWeatherData(city);
                // Perform the weather data update based on the searched city
                updateCurrentWeatherData(city);
            }
        });
    }

    private void to7Days() {
        TextView next7DaysBtn = findViewById(R.id.tv7days);
        next7DaysBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SecondActivity.class)));
    }

    private void toSavedList() {
        TextView next7DaysBtn = findViewById(R.id.savedList);
        next7DaysBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ThirdActivity.class)));
    }

    private void initRecyclerView() {
        ArrayList<HourlyForecast> items = new ArrayList<>();

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
                        // Show a toast indicating that the city does not exist
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "City not found. Please enter a valid city.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
            JSONArray weatherArray = jsonObject.optJSONArray("weather");

            if (mainObject != null && weatherArray != null && weatherArray.length() > 0) {
                double temperatureKelvin = mainObject.optDouble("temp");
                double temperatureCelsius = temperatureKelvin - 273.15;
                int humidity = mainObject.optInt("humidity");
                double temperatureMaxKelvin = mainObject.optDouble("temp_max");
                double temperatureMinKelvin = mainObject.optDouble("temp_min");
                double windSpeed = windObject.optDouble("speed");
                double pressure = mainObject.optDouble("pressure");
                String description = weatherArray.getJSONObject(0).optString("description");
                //Extracting the timestamp and converting it to readable date
                int timestamp = jsonObject.optInt("dt");
                Date date = new Date(timestamp * 1000L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(date);

                //Retrieve weather icon
                String iconCode = weatherArray.getJSONObject(0).optString("icon");
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + ".png";

                WeatherData weatherData = new WeatherData(temperatureCelsius, humidity, temperatureMaxKelvin - 273.15, temperatureMinKelvin - 273.15, windSpeed, pressure, description, formattedDate, iconUrl);
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
                String weatherIconCode = "unknown";

                if (weatherArray != null && weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    weatherIconCode = weatherObject.optString("icon");
                }

                String weatherIcon = getWeatherIconUrl(weatherIconCode);

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
    //New edit
    private String getWeatherIconUrl(String iconCode){
        return "https://openweathermap.org/img/wn/" + iconCode + ".png";
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
        String description = weatherData.getDescription();
        String currentDate = weatherData.getCurrentDate();
        String iconUrl = weatherData.getIconUrl();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //New edit
                currDateTextView.setText(currentDate);
                descriptionTextView.setText(description);
                temperatureTextView.setText(temperatureFahrenheit + "°F");
                humidityTextView.setText("Humidity: \n" + humidity + "%");
                String highLowTemperature = "H: " + highTemperatureFahrenheit + "°  L: " + lowTemperatureFahrenheit + "°";
                tvHL.setText(highLowTemperature);
                windTextView.setText("Wind: \n" + windSpeed + " km/h");
                pressureTextView.setText("Pressure: \n" + pressure + " hPa");

                ImageView ivCurrWeather = findViewById(R.id.ivCurrWeather);
                int iconSize = getResources().getDimensionPixelSize(R.dimen.main_weather_icon_size);

                Glide.with(MainActivity.this)
                        .load(iconUrl)
                        .transform(new CenterCrop(), new FitCenter()) // Apply CenterCrop and FitCenter transformations
                        .override(iconSize, iconSize) // Set the desired width and height
                        .into(ivCurrWeather);
            }
        });
    }
}