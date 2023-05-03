package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.vm
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PantallaPrincipalViewModel : ViewModel() {

    var completed = mutableStateOf(false)

    var username =   mutableStateOf("stranger")

    val selectedIndex =  mutableStateOf(0)
    val selectedDayCalories = mutableStateOf(0)

    val userCaloricGoal = mutableStateOf(0)
    val calorieDayPercentage = mutableStateOf(0)

    var formatter = DateTimeFormatter.ofPattern("EEEE d'th of' MMMM 'of' yyyy ", Locale.ENGLISH)
    var currentDate : String = ""

    var dies = mutableStateListOf<String>()
    var diesNum = mutableStateListOf<String>()

    val calId = mutableStateOf("")
    val weekId = mutableStateOf("")

    val gettingAPIValues = mutableStateOf(false)

    val mealsFromDay = MutableList(4){
        hashMapOf<RecipeCustom, Int>()
    }

    fun getMealsFromDay(
        weekId : String,
        day : Int
    ) {


        FirebaseDatabase.getInstance().reference.root.child("Weeks")
            .child(weekId)
            .child("days")
            .child(day.toString())
            .child("meals")
            .get()
            .addOnCompleteListener { meals ->

                for (i in 0 until mealsFromDay.size){
                    mealsFromDay[i].clear()
                }

                if (meals.result.exists()) {

                    val mealsInDay = meals.result.value as HashMap<Any, Any>



                    mealsInDay.forEach { meal ->
                        var count = 0

                        when(meal.key){

                            "breakfast" ->count =  0
                            "lunch" -> count = 1
                            "dinner" -> count = 2
                            "snack" -> count = 3
                        }



                        val mealsArr = meal.value as HashMap<Any, Any>
                        val mealsKeyList = mealsArr.keys.toList()
                        val mealsServingsList = mealsArr.values.toList()
                        for (i in 0 until mealsKeyList.size) {

                            Log.d("getMealsFromDay mealKey", mealsKeyList[i].toString())

                            FirebaseDatabase.getInstance().reference.root
                                .child("EdamamRecipes")
                                .child(mealsKeyList[i].toString())
                                .get()
                                .addOnCompleteListener {
                                    when(meal.key){

                                        "breakfast" ->count =  0
                                        "lunch" -> count = 1
                                        "dinner" -> count = 2
                                        "snack" -> count = 3
                                    }
                                    Log.d("getMealsFromDay inside", "count -> " + count.toString())
                                    var customRecipe = RecipeCustom()

                                    if(it.result.value != null){
                                        customRecipe.parseRecipeCustom(it.result.value as HashMap<Any, Any>)
                                        if(mealsServingsList[i] != null){
                                            mealsFromDay[count].put(customRecipe, (mealsServingsList[i] as String).toInt())
                                        }

                                    }

                                }


                        }


                    }

                }
                else{
                    for (i in 0 until mealsFromDay.size){
                        mealsFromDay[i].clear()
                    }

                }



            }




    }

    fun settingUp() {

        val authenticator = vm.authenticator
        val vmRecipes = vm.recipesViewModel

        try {
            runBlocking {
                authenticator.getUserUsername(username)

                val db = RealtimeDatabase()

                db.getCalendarId(authenticator.currentUID.value, calId)

                if (calId.value != "") {
                    currentDate = LocalDate.now().format(formatter)
                    db.getCalendarWeekId(authenticator.currentUID.value, calId, weekId)


                    if (weekId.value != "") {

                        Log.d("PantallaPrincipal", "iniciando")
                        db.changeDay(authenticator.currentUID.value, calId.value)
                        Log.d("PantallaPrincipal", "getWeekDateInDate")
                        db.getWeekDateInDate(calId.value, dies, diesNum)
                        db.getUsersCalorieGoal()
                        //getMealsFromDay( weekId.value, 0)
                        if(!gettingAPIValues.value){
                            Log.d("PantallaPrincipal", "gettingRecipes")
                            vmRecipes.get()
                            gettingAPIValues.value = true
                        }

                        //Update day calories
                        getDayCalories()
                        //Update percentage
                        getCaloriePercentage()


                    }
                }


            }
        } catch (e: Exception) {
            Log.d("settingUp", e.message.toString())
        }


    }

    fun calcMealCalories(recipes: HashMap<RecipeCustom, Int>) : Int {
        var totalCalories = 0;

        val recipeList = recipes.keys.toList()
        val servingsList = recipes.values.toList()

        for (i in 0 until recipeList.size){
            totalCalories += (recipeList[i].kcalsPerServing * servingsList[i])
        }

        return totalCalories
    }

    fun getDayCalories(){

        var cals = 0

        for (i in 0 until mealsFromDay.size){
            val recipesList = mealsFromDay[i].keys.toList()
            val servingList = mealsFromDay[i].values.toList()
            for (j in 0 until mealsFromDay[i].size){
                cals += (recipesList[j].kcalsPerServing*servingList[j])
            }
        }
        Log.d("getDayCalories", cals.toString())
        selectedDayCalories.value = cals

        Log.d("getDayCalories", selectedDayCalories.value.toString())

    }

    fun getCaloriePercentage() {

        if(userCaloricGoal.value > 0){
            Log.d("getCaloriePercentage", "mayor a 0")
            Log.d("getCaloriePercentage", selectedDayCalories.value.toString())
            Log.d("getCaloriePercentage", userCaloricGoal.value.toString())
            calorieDayPercentage.value = ((selectedDayCalories.value.toDouble() / userCaloricGoal.value) * 100).toInt()

            Log.d("getCaloriePercentage", (selectedDayCalories.value.toDouble() / userCaloricGoal.value).toString())
        }
        else{
            calorieDayPercentage.value = 0
        }


        Log.d("getCaloriePercentage", calorieDayPercentage.value.toString())
    }
}