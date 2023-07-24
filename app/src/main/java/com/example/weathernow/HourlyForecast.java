package com.example.weathernow;

public class HourlyForecast {
    private String hour;
    private int temp;
    private String path;
    public HourlyForecast(String hour, int temp, String path) {
        this.hour = hour;
        this.temp = temp;
        this.path = path;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
