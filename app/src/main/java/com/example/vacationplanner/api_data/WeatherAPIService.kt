package com.example.vacationplanner.api_data

import com.example.vacationplanner.api_data.response.APIResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "5699730c4a635c04e2f0edf20b7fb368"

interface WeatherAPIService {
    @GET("forecast")
    fun getWeatherForecast(
        @Query("q") city : String,
        @Query("units") measurement : String = "metric"
    ) : Deferred<APIResponse>

    companion object {
        operator fun invoke() : WeatherAPIService {
            val requestInterceptor =  Interceptor { chain ->
                val url = chain.request().url().newBuilder().addQueryParameter("appid", API_KEY).build()
                val request = chain.request().newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherAPIService::class.java)
        }
    }
}