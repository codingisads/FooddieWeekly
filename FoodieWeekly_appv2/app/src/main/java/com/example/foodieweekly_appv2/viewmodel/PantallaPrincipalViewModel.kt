package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.vm
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class PantallaPrincipalViewModel : ViewModel() {

    var username =   mutableStateOf("stranger")

    var selectedCalendarIndex = mutableStateOf(0)

    val selectedDayIndex =  mutableStateOf(0)
    val selectedDayCalories = mutableStateOf(0)

    val userCaloricGoal = mutableStateOf(0)
    val calorieDayPercentage = mutableStateOf(0)

    var formatter = DateTimeFormatter.ofPattern("EEEE d'th of' MMMM 'of' yyyy ", Locale.ENGLISH)
    var currentDate : String = ""

    var dies = mutableStateListOf<String>()
    var diesNum = mutableStateListOf<String>()

    val selectedIndexCalendarId = mutableStateOf("")
    val selectedIndexCalendarWeekId = mutableStateOf("")

    val gettingAPIValues = mutableStateOf(false)


    val weekMealsList : MutableState<MutableList<MutableList<HashMap<RecipeCustom, Int>>>> =
        mutableStateOf(MutableList(7){
            MutableList(4){
                hashMapOf()
            }
        })





    fun settingUp(){
        val authenticator = vm.authenticator
        val vmRecipes = vm.recipesViewModel
        val firebase = FirebaseDatabase.getInstance().reference.root

        try {
            authenticator.getUserUsername(username)

            Log.d("StartingAppMain", "uid -> " + authenticator.currentUID.value)

            Log.d("StartingAppMain", "getting calendarId")
            firebase.child("Users").child(authenticator.currentUID.value)
                .child("calendarIdList").get().addOnCompleteListener {
                    var calendar = it.result.children

                    if (it.result.childrenCount >= 1) {
                        var selectedCalendarId =
                            calendar.elementAt(selectedCalendarIndex.value).value
                        Log.d("getFirstCalendarId", selectedCalendarId.toString())

                        selectedIndexCalendarId.value = selectedCalendarId.toString();

                        currentDate = LocalDate.now().format(formatter)


                        Log.d("StartingAppMain", "getting weekId")
                        firebase.child("Calendars").child(selectedIndexCalendarId.value)
                            .child("currentWeekId").get()
                            .addOnCompleteListener {
                                selectedIndexCalendarWeekId.value = it.result.value.toString()

                                Log.d("StartingAppMain", "changing days")
                                firebase.child("Weeks").child(selectedIndexCalendarWeekId.value)
                                    .child("days").get()
                                    .addOnCompleteListener { day ->

                                        if (day.result.value != null) {

                                            val res = day.result.value as ArrayList<Any>

                                            val res1 = res.get(1) as java.util.HashMap<*, *>

                                            Log.d("changeDay", "res1 + " + res1.get("date"))

                                            val date = res1.get("date").toString().replace('/', '-')

                                            val formatter =
                                                DateTimeFormatter.ofPattern("E/d", Locale.ENGLISH)
                                            val formatterDays =
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd",
                                                    Locale.ENGLISH)

                                            val dateNow = LocalDate.now()
                                            val dateLate = LocalDate.parse(date as CharSequence?)

                                            Log.d("StartingAppMain changeDay",
                                                "dateLate + " + dateLate.toString())

                                            val diference = dateLate.until(dateNow, ChronoUnit.DAYS)

                                            Log.d("StartingAppMain changeDay",
                                                "diference + " + diference.toString())

                                            val latestDate = dateLate.plusDays(5);
                                            Log.d("StartingAppMain changeDay",
                                                "latestDate +" + latestDate.toString())

                                            //if(LocalDate.now().minusDays(LocalDate.from(res2)))


                                            val newArray = res.toMutableList()

                                            for (i in 0 until diference.toInt()) {
                                                newArray.removeAt(0)

                                                val toAddDate = latestDate.plusDays(i.toLong() + 1);

                                                Log.d("StartingAppMain changeDay",
                                                    "toAddDate + " + toAddDate.toString())

                                                val newEntry = mutableMapOf<String, String>()
                                                newEntry["date"] = toAddDate.format(formatterDays)
                                                newEntry["dateInDate"] = toAddDate.format(formatter)
                                                newArray.add(newEntry)
                                            }

                                            Log.d("StartingAppMain changeDay", "trying to add")

                                            //if there's changes, set new values
                                            if (!newArray.containsAll(res)) {
                                                firebase.child("Weeks")
                                                    .child(selectedIndexCalendarWeekId.value)
                                                    .child("days").setValue(newArray)
                                                    .addOnCompleteListener {

                                                        Log.d("StartingAppMain changeDay",
                                                            "done adding")


                                                    }
                                            }

                                            dies.clear()
                                            diesNum.clear()

                                            // getting date in date
                                            for (i in 0 until 7) {
                                                val tmp = newArray[i] as MutableMap<String, String>
                                                val child = tmp["dateInDate"]
                                                Log.d("getWeekDateInDatefun",
                                                    "child: " + child.toString())

                                                val childSeparated = child.toString().split('/')
                                                dies.add(childSeparated[0])
                                                Log.d("getWeekDateInDatefun", childSeparated[0])
                                                diesNum.add(childSeparated[1])
                                                Log.d("getWeekDateInDatefun", childSeparated[1])
                                            }

                                            //getting meals from week


                                            for (i in 0 until weekMealsList.value.size) {
                                                for (j in 0 until weekMealsList.value[i].size) {

                                                    weekMealsList.value[i][j].clear()
                                                }

                                            }

                                            //newArray.size == 7
                                            for (i in 0 until newArray.size) {
                                                val tmp = newArray[i] as MutableMap<Any, Any>
                                                if (tmp["meals"] != null) {
                                                    val meals = tmp["meals"] as HashMap<Any, Any>

                                                    //meals.size == 4

                                                    if (!meals.isNullOrEmpty()) {
                                                        meals.forEach { meal ->
                                                            var count = 0


                                                            val mealsArr =
                                                                meal.value as HashMap<Any, Any>
                                                            val mealsKeyList =
                                                                mealsArr.keys.toList()
                                                            val mealsServingsList =
                                                                mealsArr.values.toList()

                                                            for (k in 0 until mealsKeyList.size) {

                                                                Log.d("getMealsFromDay mealKey",
                                                                    mealsKeyList[k].toString())

                                                                FirebaseDatabase.getInstance().reference.root
                                                                    .child("EdamamRecipes")
                                                                    .child(mealsKeyList[k].toString())
                                                                    .get()
                                                                    .addOnCompleteListener {
                                                                        when (meal.key) {

                                                                            "breakfast" -> count = 0
                                                                            "lunch" -> count = 1
                                                                            "dinner" -> count = 2
                                                                            "snack" -> count = 3
                                                                        }
                                                                        Log.d("getMealsFromDay inside",
                                                                            "count -> " + count.toString())
                                                                        var customRecipe =
                                                                            RecipeCustom()

                                                                        if (it.result.value != null) {
                                                                            customRecipe.parseRecipeCustom(
                                                                                it.result.value as HashMap<Any, Any>)
                                                                            if (mealsServingsList[k] != null) {
                                                                                weekMealsList.value[i][count].put(
                                                                                    customRecipe,
                                                                                    (mealsServingsList[k] as String).toInt())
                                                                            }

                                                                        }

                                                                    }


                                                            }


                                                        }


                                                    }


                                                }


                                            }

                                            //getting caloric goals
                                            firebase
                                                .child("Users")
                                                .child(vm.authenticator.currentUID.value)
                                                .child("caloricGoal")
                                                .get()
                                                .addOnCompleteListener {
                                                    if (it.result.exists()) {
                                                        if (it.result.value != null) {
                                                            vm.pantallaPrincipalViewModel.userCaloricGoal.value =
                                                                (it.result.value as Long).toInt()
                                                        }
                                                    }
                                                }

                                            //getting shoppingList
                                            firebase
                                                .child("Users")
                                                .child(vm.authenticator.currentUID.value)
                                                .child("shoppingList")
                                                .get()
                                                .addOnCompleteListener {
                                                    list ->

                                                    if(list.result.value != null){
                                                        vm.shoppingViewModel.parseShoppingListFromFirebase(list.result.value as HashMap<String, String>)
                                                    }
                                                }


                                            //getting api values

                                            vm.recipesViewModel.get()
                                            gettingAPIValues.value = true




                                            vm.navController.navigate(Destinations.PantallaPrincipal.ruta)


                                        }


                                    }


                            }


                    }
                }
        }
        catch (e : Exception){
            Log.d("Starting", e.message.toString())
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

        //4
        for (i in 0 until weekMealsList.value[selectedDayIndex.value].size){
            val recipesList = weekMealsList.value[selectedDayIndex.value][i].keys.toList()
            val servingList = weekMealsList.value[selectedDayIndex.value][i].values.toList()
            for (j in 0 until weekMealsList.value[selectedDayIndex.value][i].size){
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