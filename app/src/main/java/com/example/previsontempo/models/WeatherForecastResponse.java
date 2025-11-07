package com.example.previsontempo.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.text.ParseException;

public class WeatherForecastResponse {

    @SerializedName("list")
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }

    public static class Main {
        @SerializedName("temp_max")
        private double tempMax;

        public double getTempMax() {
            return tempMax;
        }

    }

    public static class Weather {
        @SerializedName("description")
        private String description;

        public String getDescription() {
            return description;
        }

    }

    public static class ForecastItem {

        @SerializedName("dt_txt")
        private String dateText;

        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<Weather> weather;

        public String getDateText() { return dateText; }
        public Main getMain() { return main; }
        public List<Weather> getWeather() { return weather; }

        public String getMax() {
            if (main != null) {
                return String.format("%.0f°", main.getTempMax());
            }
            return "N/A";
        }

        public String getDescription() {
            if (weather != null && !weather.isEmpty() && weather.get(0) != null) {
                return weather.get(0).getDescription();
            }
            return "N/A";
        }

        public String getWeekday() {
            if (dateText != null) {
                try {
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    Date date = originalFormat.parse(dateText);

                    SimpleDateFormat targetFormat = new SimpleDateFormat("EEE", new Locale("pt", "BR"));

                    return targetFormat.format(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                    return "N/A";
                }
            }
            return "N/A";
        }
    }
}