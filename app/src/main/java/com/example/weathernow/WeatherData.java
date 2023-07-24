package com.example.weathernow;

public class WeatherData {
    private double temperature;
    private int humidity;
    private double highTemperature;
    private double lowTemperature;
    private double windSpeed;
    private double pressure;
    private String description;

    private String currentDate;

    //New edit
    private String iconUrl;

    public WeatherData(double temperatureCelsius, int humidity, double highTemperatureCelsius, double lowTemperatureCelsius, double windSpeed, double pressure, String description, String currentDate, String iconUrl) {
        this.temperature = temperatureCelsius;
        this.humidity = humidity;
        this.highTemperature = highTemperatureCelsius;
        this.lowTemperature = lowTemperatureCelsius;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.description = description;
        this.currentDate = currentDate;
        this.iconUrl = iconUrl;
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

    public String getDescription() {
        return description;
    }

    public String getCurrentDate(){
        return currentDate;
    }
    public String getIconUrl(){
        return iconUrl;
    }

}
