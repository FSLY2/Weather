package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.repository.MainRepository
import com.example.weather.models.CitiesDataModel
import com.example.weather.models.CityItem
import com.example.weather.models.Data
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

    private var _searchCitiesLiveData = MutableLiveData<List<CityItem>>()
    val searchCitiesLiveData: LiveData<List<CityItem>>
        get() = _searchCitiesLiveData

    private var allCities: List<CityItem> = emptyList()

    fun getCities() {
        getCitiesData()
    }

    fun getPrediction(query: String) {
        searchCityPrediction(query)
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

                    allCities = parseCities(citiesData.data)
                    _searchCitiesLiveData.postValue(allCities)
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

    // Convert to the required format city/country in each card of RecyclerView in the search
    private fun parseCities(countryDataList: List<Data>): List<CityItem> {
        return countryDataList.flatMap { countryData ->
            countryData.cities.map { city ->
                CityItem(city, countryData.country)
            }
        }
    }

    // Prediction list in SearchFragment
    private fun searchCityPrediction(query: String) {
        if (query.isEmpty()) {
            _searchCitiesLiveData.postValue(allCities)
        } else {
            val filteredList = allCities.filter {
                it.city.contains(query, ignoreCase = true) ||
                        it.country.contains(query, ignoreCase = true)
            }
            _searchCitiesLiveData.postValue(filteredList)
        }

    }
}