package com.example.android.connectedweather.utils;

import com.example.android.connectedweather.data.ForecastData;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ForecastUtils {
    public static ArrayList<ForecastData> parseForecastResults(String json) {
        ArrayList<ForecastData> forecastList = new ArrayList<ForecastData>();
        Gson gson = new Gson();
        OpenWeatherResult weatherJson = gson.fromJson(json, OpenWeatherResult.class);
        for(int i = 0;i < weatherJson.list.length; i++){
            ForecastData day = new ForecastData();
            day.forecast = parseResult(weatherJson.list[i]);
            day.detailedForecast = parseDetailed(weatherJson.list[i]);
            day.date = parseDate(weatherJson.list[i]);
            day.share = parseDetailed(weatherJson.list[i]);
            forecastList.add(day);
        }
        return forecastList;
    }
    static String parseResult(OpenWeatherObject forecast){
        double kelvin = forecast.main.temp- 273.15;
        DecimalFormat df = new DecimalFormat("#.##");
        String temp = df.format(kelvin) + "C";
        String date = forecast.dt_txt;
        String weather = forecast.weather[0].main;
        String output = date + " - " + weather + " - " + temp;
        return output;
    }
    static String parseDetailed(OpenWeatherObject forecast){
        DecimalFormat df = new DecimalFormat("#.##");
        double min = forecast.main.temp_min - 273.15;
        double max = forecast.main.temp_max - 273.15;
        double feel = forecast.main.feels_like - 273.15;
        String weather = forecast.weather[0].description;
        String detailed = "There will be " + weather + "with a max of " + df.format(max) +
                "C and a min of " + df.format(min) + "C which will feel like " +
                df.format(feel) + "C in Corvallis, OR.";
        return detailed;
    }
    static String parseDate(OpenWeatherObject forecast){
        String date = "Forecast on " + forecast.dt_txt;
        return date;
    }
    static class OpenWeatherResult {
        OpenWeatherObject[] list;
    }
    static class OpenWeatherObject{
        String dt_txt;
        OpenWeatherMain main;
        OpenWeatherWeather[] weather;
    }
    static class OpenWeatherMain{
        double temp;
        double feels_like;
        double temp_min;
        double temp_max;
    }
    static class OpenWeatherWeather{
        String main;
        String description;
    }

}
