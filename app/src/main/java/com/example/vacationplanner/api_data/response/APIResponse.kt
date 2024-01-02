package com.example.vacationplanner.api_data.response


data class APIResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherDetails>,
    val city: City
)