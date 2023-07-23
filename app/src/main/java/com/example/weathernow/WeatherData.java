package com.example.weathernow;

public class WeatherData {
    private double temperature;
    private int humidity;
    private double highTemperature;
    private double lowTemperature;
    private double windSpeed;
    private double pressure;

    public WeatherData(double temperatureCelsius, int humidity, double highTemperatureCelsius, double lowTemperatureCelsius, double windSpeed, double pressure) {
        this.temperature = temperatureCelsius;
        this.humidity = humidity;
        this.highTemperature = highTemperatureCelsius;
        this.lowTemperature = lowTemperatureCelsius;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getHighTemperature() {
        return highTemperature;
    }

    public double getLowTemperature() {
        return lowTemperature;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public double getPressure() {
        return pressure;
    }
}
