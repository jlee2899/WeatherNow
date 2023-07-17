package com.example.weathernow;

public class TomorrowDomain {
    private String day;
    private String picture;
    private int highTemp;
    private int lowTemp;

    public TomorrowDomain(String day, String picture, int highTemp, int lowTemp) {
        this.day = day;
        this.picture = picture;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }
}
