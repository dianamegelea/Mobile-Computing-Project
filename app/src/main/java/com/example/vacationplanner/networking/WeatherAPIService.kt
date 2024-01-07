package com.example.vacationplanner.networking

import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city : String,
        @Query("units") measurement : String = "metric"
    ) : WeatherAPIResponse
}