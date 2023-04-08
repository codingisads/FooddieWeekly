package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self")
    val self: Self
)