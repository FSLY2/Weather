package com.example.weather.models

data class WeatherDataModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)