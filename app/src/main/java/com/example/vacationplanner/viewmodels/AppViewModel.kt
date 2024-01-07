package com.example.vacationplanner.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import com.example.vacationplanner.api_data.response.WeatherDetails
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.repository.VacationRepository
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.view.WeatherAdapter

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

//    @SuppressLint("NotifyDataSetChanged")
//    fun setVacationAdapterData(data : ArrayList<VacationData>){
//        vacationAdapter.setData(data)
//        vacationAdapter.notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setWeatherAdapterData(data : ArrayList<WeatherDetails>){
//        weatherAdapter.setData(data)
//        weatherAdapter.notifyDataSetChanged()
//    }
}