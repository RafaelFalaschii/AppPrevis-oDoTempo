package com.example.previsontempo.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Double> latitude = new MutableLiveData<>();
    private final MutableLiveData<Double> longitude = new MutableLiveData<>();
    private final MutableLiveData<String> cityName = new MutableLiveData<>();

    public void setLocation(double lat, double lon) {
        latitude.setValue(lat);
        longitude.setValue(lon);
    }

    public void postLocation(double lat, double lon) {
        latitude.postValue(lat);
        longitude.postValue(lon);
    }

    public void setCityName(String city) {
        cityName.setValue(city);
    }

    public void postCityName(String city) {
        cityName.postValue(city);
    }

    public LiveData<Double> getLatitude() {
        return latitude;
    }

    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public LiveData<String> getCityName() {
        return cityName;
    }

    public void setLocationAndCity(double lat, double lon, String city) {
        latitude.setValue(lat);
        longitude.setValue(lon);
        cityName.setValue(city);
    }

    public boolean hasLocation() {
        return latitude.getValue() != null && longitude.getValue() != null;
    }

    public void clearLocation() {
        latitude.setValue(null);
        longitude.setValue(null);
        cityName.setValue(null);
    }
}