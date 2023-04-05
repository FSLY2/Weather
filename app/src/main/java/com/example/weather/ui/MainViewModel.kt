package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.repository.MainRepository
import com.example.weather.models.GeoCodeModel
import com.example.weather.models.WeatherDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val repository: MainRepository) : ViewModel() {

//    private val geoCodeLiveData = MutableLiveData<List<GeoCodeModel>>()
    private val weatherLiveData = MutableLiveData<WeatherDataModel>()

    fun getWeatherForecast(cityName: String) {
        getGeoCode(cityName)
    }

    // Receiving lat and lon from the internet. Put this coordinates in method getWeatherData() that is called in onNext()
    private fun getGeoCode(cityName: String) {
        repository.getGeoCodeData(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<GeoCodeModel>> {
                override fun onSubscribe(d: Disposable) {
                    RxJavaPlugins.setErrorHandler { error ->
                        Log.e(
                            "MainViewModel",
                            "onSubscribe: Error subscribing to observable", error
                        )
                    }
                }

                override fun onNext(geoCodeModel: List<GeoCodeModel>) {
                    val geoCode = geoCodeModel[0]
                    val lat = geoCode.lat
                    val lon = geoCode.lon
                    Log.d("MainViewModel", "onNext: LAT: $lat, LON: $lon")
//                    geoCodeLiveData.postValue(geoCodeModel)
                    getWeatherData(lat, lon)
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        "MainViewModel",
                        "ViewModel: Error getting geo code data: ${e.message}"
                    )
                }

                override fun onComplete() {
                    Log.d("MainViewModel", "ViewModel: Geo API data received successfully")
                }
            })
    }

    // Receiving weather data from the internet. Put all data in LiveData to display it in our MainFragment.
    private fun getWeatherData(lat: Double, lon: Double) {
        Log.d("MainViewModel", "getWeatherData: LAT: $lat, LON: $lon")
        repository.getWeatherData(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WeatherDataModel> {
                override fun onSubscribe(d: Disposable) {
                    RxJavaPlugins.setErrorHandler { error ->
                        Log.e(
                            "MainViewModel",
                            "onSubscribe: Error subscribing to observable", error
                        )
                    }
                }

                override fun onNext(weatherData: WeatherDataModel) {
                    val temperature = weatherData.main.temp
                    Log.d("MainViewModel", "onNext: Temperature is: $temperature")
                    weatherLiveData.postValue(weatherData)
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        "MainViewModel",
                        "ViewModel: Error getting geo code data: ${e.message}"
                    )
                }

                override fun onComplete() {
                    Log.d(
                        "MainViewModel",
                        "ViewModel: Weather API data received successfully"
                    )
                }
            })
    }

//    fun getGeoCodeLiveData(): LiveData<List<GeoCodeModel>> {
//        return geoCodeLiveData
//    }

    fun getWeatherLiveData(): LiveData<WeatherDataModel> {
        return weatherLiveData
    }
}
