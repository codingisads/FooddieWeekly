package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class LARGE(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)