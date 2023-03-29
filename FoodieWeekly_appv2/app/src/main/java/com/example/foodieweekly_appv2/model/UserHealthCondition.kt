package com.example.foodieweekly_appv2.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class UserHealthCondition {

    var isMale : Boolean = false
    var weight : String = ""
    var height : String = ""
    var activeLevel : Int = -1
    var birthdate : String = ""

    var userPreferences = mutableListOf<HealthLabels>()

    var userPreferencesStr = mutableListOf<String>()


    var preferencesToList = fun() : Unit  {
        userPreferences.forEach {
            label ->
            userPreferencesStr.add(label.name)
        }
    }


}