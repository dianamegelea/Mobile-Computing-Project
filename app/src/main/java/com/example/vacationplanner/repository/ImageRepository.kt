package com.example.vacationplanner.repository

import androidx.annotation.WorkerThread
import com.example.vacationplanner.networking.GoogleCustomSearchService
import com.example.vacationplanner.networking.ImageDownloadClient

class ImageRepository {
    private val retrofit: GoogleCustomSearchService =
        ImageDownloadClient.getRetrofitInstance().create(GoogleCustomSearchService::class.java)

    private suspend fun searchImage(
        query: String,
        apiKey: String,
        cx: String,
        searchType: String = "image"
        ) = retrofit.searchImages(query, apiKey, cx, searchType)

    @WorkerThread
    suspend fun downloadBestImage(cityName: String) = searchImage("most popular tourist attraction in " + cityName,
            "AIzaSyBj7YqeG3o2r0z4KoSN1rXWuh-EC0iJQeI",
            "63e20becb9609444f")

}


