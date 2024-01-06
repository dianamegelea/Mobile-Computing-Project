package com.example.vacationplanner.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import com.example.vacationplanner.api_data.response.WeatherDetails
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.view.WeatherAdapter

class AppViewModel(
    imageRepository: ImageRepository,
    val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _weatherForecast = MutableLiveData<WeatherAPIResponse>()
//    private val weatherForecast : LiveData<WeatherAPIResponse> = _weatherForecast
//
//    private var weatherAdapter: WeatherAdapter = WeatherAdapter()
    private var vacationAdapter: VacationAdapter = VacationAdapter()

    init {
        vacationAdapter.imageRepository = imageRepository
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