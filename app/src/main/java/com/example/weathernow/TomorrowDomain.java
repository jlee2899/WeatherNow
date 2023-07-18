package com.example.weathernow;

public class TomorrowDomain {
    private String day;
    private String picture;
    private String highTemp;
    private String lowTemp;

    public TomorrowDomain(String day, String picture, String highTemp, String lowTemp) {
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

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }
}
