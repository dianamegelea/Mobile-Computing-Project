package com.example.vacationplanner.api_data.response


import com.google.gson.annotations.SerializedName

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)