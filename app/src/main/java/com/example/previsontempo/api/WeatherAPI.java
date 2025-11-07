package com.example.previsontempo.api;

import com.example.previsontempo.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather")
    Call<WeatherResponse> getWeatherByCity(
            @Query("key") String apiKey,
            @Query("city_name") String cityName,
            @Query("format") String format
    );

    @GET("weather")
    Call<WeatherResponse> getWeatherByCoordinates(
            @Query("key") String apiKey,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("format") String format
    );
}