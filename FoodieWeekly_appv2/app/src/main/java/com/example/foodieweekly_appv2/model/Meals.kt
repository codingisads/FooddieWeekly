package com.example.foodieweekly_appv2.model

import com.example.foodieweekly_appv2.model.enums.MealType

class Meals {
    var name : String = ""
    var recipes : MutableList<Recipes> = mutableListOf<Recipes>()

    init{

    }

}