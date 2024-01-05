package com.example.vacationplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(
                imageRepository = ImageRepository(),
                weatherRepository = WeatherRepository()
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }


}