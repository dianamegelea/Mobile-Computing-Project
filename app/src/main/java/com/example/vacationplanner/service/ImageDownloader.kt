package com.example.vacationplanner.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageDownloader {
    private const val BASE_URL = "https://www.googleapis.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: GoogleCustomSearchService = retrofit.create(GoogleCustomSearchService::class.java)

    suspend fun downloadImage(query: String, apiKey: String, cx: String): String? {
        try {
            val response = service.searchImages(query, apiKey, cx, searchType = "image")
            val items = response.items
            if (items.isNotEmpty()) {
                val imageUrl = items[0].link
                return imageUrl
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
