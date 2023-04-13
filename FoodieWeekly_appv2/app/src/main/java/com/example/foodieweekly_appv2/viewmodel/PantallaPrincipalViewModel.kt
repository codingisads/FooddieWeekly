package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PantallaPrincipalViewModel : ViewModel() {


    var countDays = 0
    var countMeals = 0
    var completed = mutableStateOf(false)

    var username =   mutableStateOf("stranger")
    val selectedIndex =  mutableStateOf(0)

    var formatter = DateTimeFormatter.ofPattern("EEEE d'th of' MMMM 'of' yyyy ", Locale.ENGLISH)
    var currentDate : String = ""

    var dies = mutableStateListOf<String>()
    var diesNum = mutableStateListOf<String>()

    val calId = mutableStateOf("")
    val weekId = mutableStateOf("")

    val gettingAPIValues = mutableStateOf(false)

    fun getMealsFromDay(
        compl: MutableState<Boolean>,
        daysAndMeals: MutableList<MutableList<MutableList<String>>>, weekId : String
    ): Unit {
        FirebaseDatabase.getInstance().reference.root.child("Weeks")
            .child(weekId).child("days")
            .get().addOnCompleteListener {
                    days ->
                Log.d("Completed", "yes")
                val daysArr = days.result.value as ArrayList<Any>

                countDays = 0;
                daysArr.forEach {
                        day ->

                    Log.d("Completed", "yes days")
                    val meals = day as HashMap<Any, Any>

                    if(meals.get("meals") != null){
                        val meal = meals.get("meals") as ArrayList<Any>

                        countMeals = 0
                        meal.forEach {
                                recipes ->

                            daysAndMeals[countDays][countMeals].clear()
                            Log.d("counting", "meals " + countMeals.toString())

                            Log.d("Completed", "yes recipes")
                            val rep = recipes as HashMap<Any, Any>
                            val recipesFromMeal = rep.get("recipes")

                            if(recipesFromMeal != null){
                                Log.d("counting", "recipesFromMeal in meal " + countMeals.toString()
                                        + " is not null")
                                val recipeing = recipesFromMeal as ArrayList<Any>


                                recipeing.forEach {

                                    Log.d("counting", "recipes " + it)
                                    daysAndMeals[countDays][countMeals].add(it.toString())
                                }


                            }



                            countMeals++
                        }

                    }

                    Log.d("counting", "days " + countDays.toString())
                    countDays++

                }


                Log.d("counting", "compleato")
                compl.value = true;

            }
    }

    @Composable
    fun settingUp(
        authenticator: Authenticator,
        daysAndMeals: MutableList<MutableList<MutableList<String>>>,
        vmRecipes : RecipesViewModel
    ) {

        authenticator.getUserUsername(username)

        val db = RealtimeDatabase()

        db.getCalendarId(authenticator.currentUID.value, calId)

        if(calId.value != ""){
            currentDate = LocalDate.now().format(formatter)
            db.getCalendarWeekId(authenticator.currentUID.value, calId, weekId)


                if(weekId.value != ""){

                    Log.d("PantallaPrincipal", "iniciando")
                    db.changeDay(authenticator.currentUID.value, calId.value)
                    Log.d("PantallaPrincipal", "getWeekDateInDate")
                    db.getWeekDateInDate(calId.value, dies, diesNum)

                    getMealsFromDay(completed, daysAndMeals, weekId.value)

                    if(!gettingAPIValues.value){
                        Log.d("PantallaPrincipal", "gettingRecipes")
                        vmRecipes.get()
                        gettingAPIValues.value = true
                    }

                }
            }

    }
}