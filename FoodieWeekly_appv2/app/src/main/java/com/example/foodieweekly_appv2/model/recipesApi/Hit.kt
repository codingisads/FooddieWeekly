package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Hit(
    @SerializedName("_links")
    val links: Links,
    @SerializedName("recipe")
    val recipe: Recipe
)