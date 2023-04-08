package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Next(
    @SerializedName("href")
    val href: String,
    @SerializedName("title")
    val title: String
)