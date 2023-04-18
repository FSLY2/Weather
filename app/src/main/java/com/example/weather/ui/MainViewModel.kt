package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.repository.MainRepository
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

    private var _weatherLiveData = MutableLiveData<WeatherDataModel>()
    val weatherLiveData: LiveData<WeatherDataModel>
        get() = _weatherLiveData

    fun getWeatherForecast(cityName: String) {
        getWeatherData(cityName)
    }

    // Receiving weather data from the internet. Put all data in LiveData to display it in our MainFragment.
    private fun getWeatherData(cityName: String) {
        Log.d("MainViewModel", "getWeatherData: City Name: $cityName")
        repository.getWeatherData(cityName)
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
                    val temperature = weatherData.current.temp_c
                    Log.d("MainViewModel", "onNext: Temperature is: $temperature")
                    _weatherLiveData.postValue(weatherData)
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
}
