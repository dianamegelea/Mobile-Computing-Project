package com.example.vacationplanner.networking

import com.example.vacationplanner.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleCustomSearchService {
    @GET("customsearch/v1")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("cx") cx: String,
        @Query("searchType") searchType: String = "image"
    ): SearchResponse
}
