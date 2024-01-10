package com.example.vacationplanner.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    application : Application,
) : ViewModel() {

    private val weatherRepository = WeatherRepository()

    val weatherLiveData = weatherRepository.mutableWeatherLiveData

    fun retrieveWeather(city : String, selectedDate : String, daysOfStay : Int, metric : String) = viewModelScope.launch {
        weatherRepository.getWeatherForecastForCity(city, selectedDate , daysOfStay, metric)
    }
}


class WeatherViewModelFactory(private val application : Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(
                application = application,
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}