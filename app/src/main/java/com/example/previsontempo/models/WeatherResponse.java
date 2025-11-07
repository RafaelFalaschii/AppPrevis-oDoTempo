package com.example.previsontempo.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("results")
    private Results results;

    public Results getResults() {
        return results;
    }

    public static class Results {
        @SerializedName("city")
        private String city;

        @SerializedName("temp")
        private int temp;

        @SerializedName("description")
        private String description;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("wind_speedy")
        private String windSpeedy;

        @SerializedName("condition_slug")
        private String conditionSlug;

        @SerializedName("forecast")
        private List<Forecast> forecast;

        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        // getters
        public String getCity() { return city; }
        public int getTemp() { return temp; }
        public String getDescription() { return description; }
        public int getHumidity() { return humidity; }
        public String getWindSpeedy() { return windSpeedy; }
        public String getConditionSlug() { return conditionSlug; }
        public List<Forecast> getForecast() { return forecast; }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}