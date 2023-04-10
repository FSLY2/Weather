package com.example.weather.data.repository

import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.core.Observable

interface MainRepository {

    fun getWeatherData(cityName: String) : Observable<WeatherDataModel>
}