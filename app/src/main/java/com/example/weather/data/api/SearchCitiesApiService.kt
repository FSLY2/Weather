package com.example.weather.data.api

import com.example.weather.models.CitiesDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface SearchCitiesApiService {

    @GET("countries")
    fun getCities(): Observable<CitiesDataModel>
}