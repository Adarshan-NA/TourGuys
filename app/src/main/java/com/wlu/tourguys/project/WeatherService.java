package com.wlu.tourguys.project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("dt") long dt,
            @Query("lang") String lang,
            @Query("units") String units
    );

    Object getWeatherData(double v, double v1, String s);
}

