package com.example.vacationplanner.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.WeatherAdapter
import com.example.vacationplanner.viewmodels.WeatherViewModel
import com.example.vacationplanner.viewmodels.WeatherViewModelFactory

class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            WeatherViewModelFactory(activity.application)
        ).get(WeatherViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val weatherAdapter = WeatherAdapter()

        val rootView = inflater.inflate(R.layout.fragment_weather, container, false)
        val recView = rootView.findViewById<RecyclerView>(R.id.forecast_recyclerview)
        recView.adapter = weatherAdapter
        recView.layoutManager = LinearLayoutManager(requireContext())

        val vacationData: VacationData? =  arguments?.getParcelable<VacationData>("vacationdata")
        if (vacationData != null) {

            val city: String = vacationData.cityName
            (activity as AppCompatActivity).supportActionBar?.title = city

            val fromDate: String = vacationData.startDate
            val days: Int = vacationData.noDays

            val metric = "metric"

            weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) { weather ->
                // Update the cached copy of the words in the adapter.
                weather.let {
                    weatherAdapter.setData(ArrayList(weather))
                    weatherAdapter.notifyDataSetChanged()
                }
            }

            weatherViewModel.retrieveWeather(city, fromDate, days, metric)
        }

        return  rootView
    }
}