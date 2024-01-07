package com.example.vacationplanner.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vacationplanner.view.VacationAdapter

class AppViewModel(
    application : Application,
    imageRepository: ImageRepository,
    vacationRepository: VacationRepository,
    val weatherRepository: WeatherRepository
) : AndroidViewModel(application) {

    private var vacationAdapter: VacationAdapter = VacationAdapter()

    init {
        vacationAdapter.imageRepository = imageRepository
        vacationAdapter.vacationRepository = vacationRepository
    }
    fun getVacationAdapter(): VacationAdapter {
        return vacationAdapter
    }
}