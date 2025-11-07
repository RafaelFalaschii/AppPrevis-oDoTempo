package com.example.previsontempo.models;

import java.util.List;

public class Forecast {

    private String date;
    private String weekday;
    private Double max;
    private Double min;
    private Double sens;
    private Double humidity;
    private List<Weather> weather;

    public Forecast() {}

    public Forecast(String date, String weekday, Double max, Double min, Double sens, Double humidity, List<Weather> weather) {
        this.date = date;
        this.weekday = weekday;
        this.max = max;
        this.min = min;
        this.sens = sens;
        this.humidity = humidity;
        this.weather = weather;
    }

    // Getters e Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getSens() {
        return sens;
    }

    public void setSens(Double sens) {
        this.sens = sens;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    // 🔹 Método que retorna a condição principal do clima (usado no ForecastAdapter)
    public String getCondition() {
        if (weather != null && !weather.isEmpty()) {
            Weather w = weather.get(0);
            if (w.getMain() != null && !w.getMain().isEmpty()) return w.getMain();
            if (w.getDescription() != null && !w.getDescription().isEmpty()) return w.getDescription();
        }
        return "Clear"; //
    }
}