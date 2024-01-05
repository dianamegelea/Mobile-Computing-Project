package com.example.vacationplanner.viewmodels

import com.example.vacationplanner.networking.GoogleCustomSearchService
import com.example.vacationplanner.networking.ImageDownloadClient

class ImageRepository {
    val retrofit =
        ImageDownloadClient.getRetrofitInstance().create(GoogleCustomSearchService::class.java)

    suspend fun searchImage(
        query: String,
        apiKey: String,
        cx: String,
        searchType: String = "image"
    ) = retrofit.searchImages(query, apiKey, cx, searchType)
}