package com.example.vacationplanner.viewmodels

import com.example.vacationplanner.networking.WeatherAPIService
import com.example.vacationplanner.networking.WeatherDataClient

class WeatherRepository {
    private val retrofit =
        WeatherDataClient.getRetrofitInstance().create(WeatherAPIService::class.java)

    suspend fun getWeatherForecastForCity(city: String, metric: String = "metric") =
        retrofit.getWeatherForecast(city, metric)
}