package com.example.vacationplanner.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vacationplanner.database.VacationDatabase

class AppViewModelFactory(private val application : Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(
                application = application,
                imageRepository = ImageRepository(),
                vacationRepository = VacationRepository(VacationDatabase.getDatabase(application)),
                weatherRepository = WeatherRepository(),
            ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }


}