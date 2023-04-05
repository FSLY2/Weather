package com.example.weather.data.api

import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather?")
    fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "ua",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = "1e4e4eadb5798888ace488f9d3fd6323"
    ) : Observable<WeatherDataModel>
}