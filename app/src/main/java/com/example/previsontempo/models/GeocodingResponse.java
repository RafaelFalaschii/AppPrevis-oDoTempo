package com.example.previsontempo.models;

import com.google.gson.annotations.SerializedName;

public class GeocodingResponse {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    @SerializedName("name")
    private String name;

    // Getters
    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}