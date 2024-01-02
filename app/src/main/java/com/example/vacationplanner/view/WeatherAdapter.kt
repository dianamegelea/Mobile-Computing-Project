package com.example.vacationplanner.view

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.api_data.GlideApp
import com.example.vacationplanner.api_data.response.WeatherDetails

class WeatherAdapter(val c: Context, val weatherList: List<WeatherDetails>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    inner class WeatherViewHolder(forecastView: View) : RecyclerView.ViewHolder(forecastView) {
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

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = weatherList[position]
        val firstLine : String = weatherData.dtTxt.substring(0, 10)
        val newLine : String = "\n"
        val secondLine : String = weatherData.dtTxt.substring(10)
        val unit = PreferenceManager.getDefaultSharedPreferences(c).getString("unit_system", "metric")
        var displayUnit : String = "°C"
        if (unit == "imperial") {
            displayUnit = "°F"
        }

        holder.weatherDate.text = "$firstLine$newLine$secondLine"
        holder.weatherDesc.text = weatherData.weather.get(0).description
        holder.weatherTemp.text = weatherData.main.temp.toString() + displayUnit
        val iconCode = weatherData.weather.get(0).icon
        val iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png"
        GlideApp.with(c)
            .load(iconUrl)
            .into(holder.weatherIcon)
    }


}