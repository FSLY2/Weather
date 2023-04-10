package com.example.weather.data.repository

import android.util.Log
import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val apiProvider: ApiProvider) : MainRepository {

    override fun getWeatherData(cityName: String): Observable<WeatherDataModel> {
        return apiProvider.provideWeatherApi().getWeatherForecast(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error -> Log.e("MainRepositoryImpl", "Error getting weather data", error) }
    }
}