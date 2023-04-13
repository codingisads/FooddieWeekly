package com.example.foodieweekly_appv2.xarxa

import com.example.foodieweekly_appv2.model.recipesApi.Recipes
import retrofit2.http.GET
import retrofit2.http.Path
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
}