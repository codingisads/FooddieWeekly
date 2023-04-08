package com.example.foodieweekly_appv2.xarxa

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecipesClient {

    const val APP_KEY = "30b326217041ba5af80d4daad690f2c4"
    const val APP_ID = "224854a5"

    const val BASE_URL = "https://api.edamam.com";

    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/pw500" //w500 mida
    const val BASE_URL_IMG_MIDA_ORIGINAL = "https://image.tmdb.org/t/pw500"


    val gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val servei = retrofit.create(EdamamService::class.java)
}