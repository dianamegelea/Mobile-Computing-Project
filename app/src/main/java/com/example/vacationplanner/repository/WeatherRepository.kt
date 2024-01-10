package com.example.vacationplanner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import com.example.vacationplanner.api_data.response.WeatherDetails
import com.example.vacationplanner.networking.WeatherAPIService
import com.example.vacationplanner.networking.WeatherDataClient
import com.example.vacationplanner.utils.Utils
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherRepository {
    private val retrofit =
        WeatherDataClient.getRetrofitInstance().create(WeatherAPIService::class.java)

    val mutableWeatherLiveData = MutableLiveData<List<WeatherDetails>>()

    private fun filterWeatherDetails(
        list: List<WeatherDetails>,
        selectedDate: String,
        daysOfStay: Int
    ): List<WeatherDetails> {
        val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val parsedDate = inputDateFormat.parse(selectedDate)
        val selectedDateString = outputDateFormat.format(parsedDate!!)

        val endDate = Utils.calculateDateAfterNDays(parsedDate, daysOfStay + 1)
        val endDateString = outputDateFormat.format(endDate)

        return list.filter { it.dtTxt >= selectedDateString && it.dtTxt < endDateString }
    }

    suspend fun getWeatherForecastForCity(city: String, selectedDate: String, daysOfStay: Int, metric: String = "metric") {
        val response = retrofit.getWeatherForecast(city, metric)

        val filteredList = filterWeatherDetails(response.list, selectedDate, daysOfStay)

        mutableWeatherLiveData.value = filteredList
    }
}