package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName("count")
    val count: Int,
    @SerializedName("from")
    val from: Int,
    @SerializedName("hits")
    val hits: List<Hit>,
    @SerializedName("_links")
    val links: LinksX,
    @SerializedName("to")
    val to: Int
)