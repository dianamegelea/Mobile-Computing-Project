package com.example.vacationplanner.api_data.response


import com.google.gson.annotations.SerializedName

data class WeatherDetails (
    val dt: Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dtTxt: String
) {
    override fun toString(): String {
        return "WeatherDetails(dt=$dt, main=$main, weather=$weather, clouds=$clouds, wind=$wind, visibility=$visibility, pop=$pop, rain=$rain, sys=$sys, dtTxt='$dtTxt')"
    }
}