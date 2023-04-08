package com.example.foodieweekly_appv2.model.recipesApi


import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("calories")
    val calories: Double,
    @SerializedName("cautions")
    val cautions: List<String>,
    @SerializedName("cuisineType")
    val cuisineType: List<String>,
    @SerializedName("dietLabels")
    val dietLabels: List<String>,
    @SerializedName("digest")
    val digest: List<Digest>,
    @SerializedName("dishType")
    val dishType: List<String>,
    @SerializedName("healthLabels")
    val healthLabels: List<String>,
    @SerializedName("image")
    val image: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("ingredientLines")
    val ingredientLines: List<String>,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("label")
    val label: String,
    @SerializedName("mealType")
    val mealType: List<String>,
    @SerializedName("shareAs")
    val shareAs: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("totalDaily")
    val totalDaily: TotalDaily,
    @SerializedName("totalNutrients")
    val totalNutrients: TotalNutrients,
    @SerializedName("totalTime")
    val totalTime: Double,
    @SerializedName("totalWeight")
    val totalWeight: Double,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("yield")
    val yield: Double
)