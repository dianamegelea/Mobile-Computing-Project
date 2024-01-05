package com.example.vacationplanner.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherDataClient {

    private const val API_KEY = "5699730c4a635c04e2f0edf20b7fb368"

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun getRetrofitInstance(): Retrofit {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request().url().newBuilder()
                .addQueryParameter("appid", API_KEY)
                .build()
            val request = chain.request().newBuilder().url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // To be movedddddd
//    operator fun invoke() : WeatherAPIService {
//        val requestInterceptor =  Interceptor { chain ->
//            val url = chain.request().url().newBuilder().addQueryParameter("appid", API_KEY).build()
//            val request = chain.request().newBuilder().url(url).build()
//            return@Interceptor chain.proceed(request)
//        }
//
//        val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()
//
//        return Retrofit.Builder()
//            .client(okHttpClient)
//            .baseUrl(BASE_URL)
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(WeatherAPIService::class.java)
//    }

}