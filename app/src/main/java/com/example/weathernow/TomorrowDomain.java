package com.example.weathernow;

public class TomorrowDomain {
    private String day;
    private String icon;
    private String highTemp;
    private String lowTemp;

    public TomorrowDomain(String day, String icon, String highTemp, String lowTemp) {
        this.day = day;
        this.icon = icon;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String picture) {
        this.icon = picture;
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
