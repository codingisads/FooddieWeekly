package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class SugarAlcohol(
    @SerializedName("label")
    val label: String,
    @SerializedName("quantity")
    val quantity: Double,
    @SerializedName("unit")
    val unit: String
)