package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.repository.MainRepository
import com.example.weather.models.CitiesDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _searchCitiesLiveData = MutableLiveData<CitiesDataModel>()
    val searchCitiesLiveData: LiveData<CitiesDataModel>
        get() = _searchCitiesLiveData

    fun getCities() {
        getCitiesData()
    }

    private fun getCitiesData() {
        repository.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CitiesDataModel> {
                override fun onSubscribe(d: Disposable) {
                    RxJavaPlugins.setErrorHandler { error ->
                        Log.e(
                            "SearchViewModel",
                            "onSubscribe: Error subscribing to observable", error
                        )
                    }
                }

                override fun onNext(citiesData: CitiesDataModel) {
                    val test = citiesData.data[0].country
                    Log.d("SearchViewModel", "onNext: Country is: $test")
                    _searchCitiesLiveData.postValue(citiesData)
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        "SearchViewModel",
                        "ViewModel: Error getting cities data: ${e.message}"
                    )
                }

                override fun onComplete() {
                    Log.d(
                        "SearchViewModel",
                        "ViewModel: Cities API data received successfully"
                    )
                }
            })
    }
}