package com.example.weather.data.repository

import com.example.weather.models.GeoCodeModel
import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.core.Observable

interface MainRepository {

    fun getWeatherData(lat: Double, lon: Double) : Observable<WeatherDataModel>

    fun getGeoCodeData(city: String) : Observable<List<GeoCodeModel>>
}