package com.example.vacationplanner.api_data.response


data class WeatherAPIResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherDetails>,
    val city: City
)