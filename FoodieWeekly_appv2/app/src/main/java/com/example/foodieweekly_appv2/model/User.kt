package com.example.foodieweekly_appv2.model

import androidx.compose.runtime.mutableStateListOf

class User () {
    var username : String = "";
    var caloricGoal : Int = 0;
    var firstName : String = "";
    var lastName : String = "";
    var preferences = mutableListOf<String>("")
    var recipes : String = ""
    var savedRecipes : String = ""
    var shoppingList : List<String> = mutableListOf("")
    var calendarIdList : List<String> = mutableListOf<String>()

    init {

    }
}