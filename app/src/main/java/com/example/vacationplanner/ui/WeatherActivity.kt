package com.example.vacationplanner.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.api_data.response.WeatherAPIResponse
import com.example.vacationplanner.api_data.response.WeatherDetails
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.utils.Utils
import com.example.vacationplanner.view.WeatherAdapter
import com.example.vacationplanner.viewmodels.WeatherViewModel
import com.example.vacationplanner.viewmodels.WeatherViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by lazy {
        val application = requireNotNull(this.application) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, WeatherViewModelFactory(application)).get(WeatherViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast_for_city)

        val weatherAdapter = WeatherAdapter()

        val recView = findViewById<RecyclerView>(R.id.forecast_recyclerview)
        recView.adapter = weatherAdapter
        recView.layoutManager = LinearLayoutManager(this)

        val vacationData: VacationData? =
            intent.getParcelableExtra("vacationdata", VacationData::class.java)

        if (vacationData != null) {

            val city: String = vacationData.cityName
            supportActionBar?.title = city
            val fromDate: String = vacationData.startDate
            val days: Int = vacationData.noDays

            val metric = "metric"

            weatherViewModel.weatherLiveData.observe(this) { weather ->
                // Update the cached copy of the words in the adapter.
                weather.let {
                    weatherAdapter.setData(ArrayList(weather))
                    weatherAdapter.notifyDataSetChanged()
                }
            }

            weatherViewModel.retrieveWeather(city, fromDate, days, metric)
        }
    }
}