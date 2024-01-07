package com.example.vacationplanner.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.api_data.GlideApp
import com.example.vacationplanner.api_data.response.WeatherDetails

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var weatherList = ArrayList<WeatherDetails>()
    fun setData(weatherList: ArrayList<WeatherDetails>) {
        this.weatherList = weatherList
    }

    inner class WeatherViewHolder(val forecastView: View) : RecyclerView.ViewHolder(forecastView) {
        val weatherDate: TextView = forecastView.findViewById(R.id.forecast_date)
        val weatherTemp: TextView = forecastView.findViewById(R.id.forecast_temperature)
        val weatherDesc: TextView = forecastView.findViewById(R.id.forecast_description)
        val weatherIcon: ImageView = forecastView.findViewById(R.id.forecast_icon)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = weatherList[position]
        val firstLine: String = weatherData.dtTxt.substring(0, 10)
        val newLine = "\n"
        val secondLine: String = weatherData.dtTxt.substring(10)
        val displayUnit = "°C"

        holder.weatherDate.text = "$firstLine$newLine$secondLine"
        holder.weatherDesc.text = weatherData.weather[0].description
        holder.weatherTemp.text = weatherData.main.temp.toString() + displayUnit
        val iconCode = weatherData.weather[0].icon
        val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"
        GlideApp.with(holder.forecastView.context)
            .load(iconUrl)
            .into(holder.weatherIcon)
    }
}