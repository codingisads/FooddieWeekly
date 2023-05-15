package com.example.foodieweekly_appv2.xarxa

import com.example.foodieweekly_appv2.model.recipesApi.Recipes
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EdamamService {

    @GET("/api/recipes/v2")
    suspend fun getRecipesOf(
        @Query("app_key") appkey : String,
        @Query("app_id") appid : String,
        @Query("q") q : String? = "",
        @Query("type") type : String = "public",
        @Query("random") random : Boolean = false,
        @Query("imageSize") imageSize : Array<String> = arrayOf("REGULAR")
    ): Recipes


    @GET()
    suspend fun getNextPage(
        @Url() path : String
    ): Recipes

    @GET("/api/recipes/v2")
    suspend fun getRecipesOfWithFilters(
        @Query("app_key") appkey : String,
        @Query("app_id") appid : String,
        @Query("q") q : String? = "",
        @Query("type") type : String = "public",
        @Query("health") h1 : String? = null,
        @Query("health") h2 : String? = null,
        @Query("health") h3 : String? = null,
        @Query("health") h4 : String? = null,
        @Query("health") h5 : String? = null,
        @Query("health") h6 : String? = null,
        @Query("health") h7 : String? = null,
        @Query("health") h8 : String? = null,
        @Query("health") h9 : String? = null,
        @Query("health") h10 : String? = null,
        @Query("health") h11 : String? = null,
        @Query("health") h12 : String? = null,
        @Query("health") h13 : String? = null,
        @Query("random") random : Boolean = false,
        @Query("imageSize") imageSize : Array<String> = arrayOf("REGULAR")
    ): Recipes

}