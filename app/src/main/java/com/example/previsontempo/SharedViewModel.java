package com.example.previsontempo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<LocationData> locationLiveData = new MutableLiveData<>();

    public LiveData<LocationData> getLocationLiveData() {
        return locationLiveData;
    }
    public void setLocationAndCity(double lat, double lon, String city) {
        LocationData data = new LocationData(lat, lon, city);
        locationLiveData.setValue(data);
    }

    public static class LocationData {
        private final double latitude;
        private final double longitude;
        private final String city;

        public LocationData(double latitude, double longitude, String city) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.city = city;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getCity() {
            return city;
        }
    }
}