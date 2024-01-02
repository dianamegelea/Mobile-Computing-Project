package com.example.vacationplanner.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items")
    val items: List<ImageItem>
)

data class ImageItem(
    @SerializedName("link")
    val link: String
)
