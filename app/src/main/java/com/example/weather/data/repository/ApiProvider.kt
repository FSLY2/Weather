package com.example.weather.data.repository

import com.example.weather.data.api.GeoCodingApiService
import com.example.weather.data.api.WeatherApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiProvider {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun provideWeatherApi(): WeatherApiService = retrofit.create(WeatherApiService::class.java)

    fun provideGeoCodeApi(): GeoCodingApiService = retrofit.create(GeoCodingApiService::class.java)
}