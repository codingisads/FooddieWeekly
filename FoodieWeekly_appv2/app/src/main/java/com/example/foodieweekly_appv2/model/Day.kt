package com.example.foodieweekly_appv2.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.foodieweekly_appv2.model.enums.MealType
import java.util.*

class Day {
    public var dateInDate : String = ""
    public var date : String = ""
    public var meals = mutableListOf<Meals>()

    init {
        MealType.values().forEach {
            it ->
                val newMeal = Meals()
                newMeal.name = it.name
                meals.add(newMeal)
        }
    }
}