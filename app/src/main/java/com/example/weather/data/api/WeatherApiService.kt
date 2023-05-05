package com.example.weather.data.api

import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast.json?")
    fun getWeatherForecast(
        @Query("q") q: String,
        @Query("days") days: Int = DAYS_COUNT,
        @Query("aqi") aqi: String = "yes",
        @Query("key") key: String = API_KEY
    ): Observable<WeatherDataModel>

    companion object {
        private const val DAYS_COUNT = 5
        private const val API_KEY = "00790a7494ae46f5a87115158230504"
    }
}