package com.example.vacationplanner

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import com.example.vacationplanner.api_data.response.WeatherDetails
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.WeatherAdapter
import com.example.vacationplanner.viewmodels.AppViewModel
import com.example.vacationplanner.viewmodels.AppViewModelFactory
import com.example.vacationplanner.viewmodels.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherForecastForCity : AppCompatActivity() {
    private lateinit var appViewModel: AppViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast_for_city)
        val recycler_view: RecyclerView = findViewById(R.id.forecast_recyclerview)
        recycler_view.layoutManager = LinearLayoutManager(this)

        val vacationData: VacationData? =
            intent.getParcelableExtra("vacationdata", VacationData::class.java)
        appViewModel = ViewModelProvider(this, AppViewModelFactory(this.application, ))[AppViewModel::class.java]
        val weatherRepository: WeatherRepository = appViewModel.weatherRepository

        if (vacationData != null) {
            val city: String = vacationData.cityName
            supportActionBar?.title = city
            val fromDate: String = vacationData.startDate
            val days: Int = vacationData.noDays

            val metric = "metric"

            var response: WeatherAPIResponse? = null
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    response = weatherRepository.getWeatherForecastForCity(city, metric)
                } catch (exception: Exception) {
                    if (exception is HttpException && exception.code() == 401)
                        print(exception.message().toString())
                }
                val weatherDetails: List<WeatherDetails> =
                    filterWeatherDetails(response!!.list, fromDate, days)
                val adapter = WeatherAdapter()
                adapter.setData(ArrayList(weatherDetails))
                recycler_view.adapter = adapter
            }

        }
    }

    private fun filterWeatherDetails(
        list: List<WeatherDetails>,
        selectedDate: String,
        daysOfStay: Int
    ): List<WeatherDetails> {
        val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val parsedDate = inputDateFormat.parse(selectedDate)
        val selectedDateString = outputDateFormat.format(parsedDate)

        val endDate = calculateDateAfterNDays(parsedDate, daysOfStay + 1)
        val endDateString = outputDateFormat.format(endDate)

        return list.filter { it.dtTxt >= selectedDateString && it.dtTxt < endDateString }
    }

    private fun calculateDateAfterNDays(D: Date, N: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = D
        calendar.add(Calendar.DAY_OF_MONTH, N)
        return calendar.time
    }

}