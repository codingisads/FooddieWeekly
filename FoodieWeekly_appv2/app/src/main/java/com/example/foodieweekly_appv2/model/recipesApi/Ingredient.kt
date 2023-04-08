package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("food")
    val food: String,
    @SerializedName("foodCategory")
    val foodCategory: String,
    @SerializedName("foodId")
    val foodId: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("measure")
    val measure: String,
    @SerializedName("quantity")
    val quantity: Double,
    @SerializedName("text")
    val text: String,
    @SerializedName("weight")
    val weight: Double
)