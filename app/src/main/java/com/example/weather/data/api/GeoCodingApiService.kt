package com.example.weather.data.api

import com.example.weather.models.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService {

    @GET("geo/1.0/direct?")
    fun getGeoCodeApi(
        @Query("q") q: String,
        @Query("appid") appid: String = "1e4e4eadb5798888ace488f9d3fd6323"
    ) : Observable<List<GeoCodeModel>>
}