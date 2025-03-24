package com.example.weather.data.repository

import com.example.weather.data.api.SearchCitiesApiService
import com.example.weather.data.api.WeatherApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiProvider {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun provideWeatherApi(): WeatherApiService = retrofit.create(WeatherApiService::class.java)

    private val retrofitCity = Retrofit.Builder()
        .baseUrl("https://countriesnow.space/api/v0.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun provideCityApi(): SearchCitiesApiService =
        retrofitCity.create(SearchCitiesApiService::class.java)
}