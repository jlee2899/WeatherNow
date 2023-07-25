package com.example.weathernow;

public class CityDataHolder {
    private static String city;

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        CityDataHolder.city = city;
    }
}
