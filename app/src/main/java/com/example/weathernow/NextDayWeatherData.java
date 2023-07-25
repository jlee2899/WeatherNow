package com.example.weathernow;

public class NextDayWeatherData {
    private double temperatureCelsiusND;
    private int humidityND;
    private double highTemperatureCelsiusND;
    private double lowTemperatureCelsiusND;
    private double windSpeedND;
    private double pressureND;
    private String descriptionND;
    private String nextDay;
    private String iconUrlND;

    // Constructor
    public NextDayWeatherData(double temperatureCelsius, int humidity, double highTemperatureCelsius, double lowTemperatureCelsius,
                              double windSpeed, double pressure, String description, String nextDay, String iconUrl) {
        this.temperatureCelsiusND = temperatureCelsius;
        this.humidityND = humidity;
        this.highTemperatureCelsiusND = highTemperatureCelsius;
        this.lowTemperatureCelsiusND = lowTemperatureCelsius;
        this.windSpeedND = windSpeed;
        this.pressureND = pressure;
        this.descriptionND = description;
        this.nextDay = nextDay;
        this.iconUrlND = iconUrl;
    }

    // Getters
    public double getTemperatureCelsiusND() {
        return temperatureCelsiusND;
    }

    public int getHumidityND() {
        return humidityND;
    }

    public double getHighTemperatureCelsiusND() {
        return highTemperatureCelsiusND;
    }

    public double getLowTemperatureCelsiusND() {
        return lowTemperatureCelsiusND;
    }

    public double getWindSpeedND() {
        return windSpeedND;
    }

    public double getPressureND() {
        return pressureND;
    }

    public String getDescriptionND() {
        return descriptionND;
    }

    public String nextDay() {
        return nextDay;
    }

    public String getIconUrlND() {
        return iconUrlND;
    }
}
