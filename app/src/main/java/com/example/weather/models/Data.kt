package com.example.weather.models

data class Data(
    val cities: List<String>,
    val country: String,
    val iso2: String,
    val iso3: String
)