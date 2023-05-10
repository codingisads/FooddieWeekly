package com.example.foodieweekly_appv2.model

data class MealsInWeek(
    var numMealsAdded : Int = 0,
    val weekMealsList : MutableList<MutableList<HashMap<RecipeCustom, Int>>> =
        MutableList(7){
            MutableList(4){
                hashMapOf()
            }
        }
)
