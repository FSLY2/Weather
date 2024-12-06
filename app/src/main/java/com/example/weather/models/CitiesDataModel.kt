package com.example.weather.models

data class CitiesDataModel(
    val data: List<Data>,
    val error: Boolean,
    val msg: String
)