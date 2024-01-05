package com.example.vacationplanner.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageDownloadClient {

    private const val BASE_URL = "https://www.googleapis.com/"

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
