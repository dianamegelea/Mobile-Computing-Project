package com.example.vacationplanner.api_data.response


import com.google.gson.annotations.SerializedName

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)