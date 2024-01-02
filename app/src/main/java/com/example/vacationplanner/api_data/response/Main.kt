package com.example.vacationplanner.api_data.response


import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("grnd_level")
    val grndLevel: Int,
    val humidity: Int,
    @SerializedName("temp_kf")
    val tempKf: Double
)