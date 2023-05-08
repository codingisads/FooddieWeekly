package com.example.foodieweekly_appv2.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.model.enums.MealType
import com.example.foodieweekly_appv2.model.recipesApi.LinksX
import com.example.foodieweekly_appv2.model.recipesApi.Next
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.model.recipesApi.Recipes
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.vm
import com.example.foodieweekly_appv2.xarxa.RecipesClient
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class RecipesViewModel : ViewModel() {

    /*private var _respostaRecipes = mutableStateOf(Recipes(, null))

    val respostaRecipes = _respostaRecipes;*/

    private var _respostaRecipes = mutableStateOf(Recipes(0, 0, emptyList(), LinksX(Next("", "")), 0))
    val respostaRecipes = _respostaRecipes

    private var _nextPageLink = mutableStateOf("")
    val nextPageLink = _nextPageLink

    private var _llistaRecipes : MutableState<MutableList<Any>> =mutableStateOf(mutableListOf())
    val llistaRecipes = _llistaRecipes

    private var _llistaSavedRecipes : MutableState<MutableList<RecipeCustom>> =mutableStateOf(mutableListOf())
    val llistaSavedRecipes = _llistaSavedRecipes

    private var _recipesXAnnotations : MutableMap<Any, Any> = mutableMapOf()
    val recipesXAnnotations = _recipesXAnnotations


    //public var userSavedRecipesIds = arrayListOf<Any>()

    lateinit var selectedRecipe : Any

    var selectedRecipeSaves = mutableStateOf(0)



    private var _addMode = mutableStateOf(false)
    public val addMode = _addMode

    public var selectedMeal = MealType.Breakfast


    fun get() {
        viewModelScope.launch(Dispatchers.IO){

            try{

                val resultat = RecipesClient.servei
                    .getRecipesOf(RecipesClient.APP_KEY,
                        RecipesClient.APP_ID)

                respostaRecipes.value = resultat

                nextPageLink.value = respostaRecipes.value.links.next.href

                Log.d("getRecipes recipes page", nextPageLink.value)

                llistaRecipes.value.clear()
                llistaRecipes.value.addAll(respostaRecipes.value.hits)


                //llistaRecipes.value = respostaRecipes.value.results
            }
            catch(e : Exception){
                Log.d("getRecipes", e.message.toString())
            }

        }


        Log.d("getRecipes returning", llistaRecipes.value.size.toString())

    }

    fun getRecipesOf(ingredient : String) {
        viewModelScope.launch(Dispatchers.IO){

            try{
                val resultat = RecipesClient.servei
                    .getRecipesOf(RecipesClient.APP_KEY,
                        RecipesClient.APP_ID, q = ingredient)

                respostaRecipes.value = resultat

                if(respostaRecipes != null){
                    nextPageLink.value = respostaRecipes.value.links.next.href
                    llistaRecipes.value.clear()
                    llistaRecipes.value.addAll(respostaRecipes.value.hits)
                }


                Log.d("getRecipes recipes page lol", nextPageLink.value)





                //llistaRecipes.value = respostaRecipes.value.results
            }
            catch(e : Exception){
                Log.d("getRecipes lol", e.message.toString())
            }

        }


        Log.d("getRecipes returning", llistaRecipes.value.size.toString())

    }

    fun getNextPage() {
        viewModelScope.launch(Dispatchers.IO){

            try{

                val resultat = RecipesClient.servei
                    .getNextPage(nextPageLink.value)

                respostaRecipes.value = resultat

                nextPageLink.value = respostaRecipes.value.links.next.href

                Log.d("getRecipes recipes page nene", nextPageLink.value)

                llistaRecipes.value.addAll(respostaRecipes.value.hits)

                Log.d("getRecipes page nene", "done")
                //llistaRecipes.value = respostaRecipes.value.results
            }
            catch(e : Exception){
                Log.d("getRecipes page nene", e.message.toString())
            }

        }


        Log.d("getRecipes returning", llistaRecipes.value.size.toString())
    }


    fun setActualRecipe(rec : Any)  {
        selectedRecipe = rec
    }


    fun getUserSavedRecipesIds() {

        llistaSavedRecipes.value.clear()

        FirebaseDatabase.getInstance().reference.root.child("Users")
            .child(vm.authenticator.currentUID.value.toString())
            .child("savedRecipes").get().addOnCompleteListener {
                var uid = vm.authenticator.currentUID
                var shild = it.result
                var key = it.result.key
                var result = it.result.value

                if(result != null && result != ""){
                    _recipesXAnnotations.putAll(result as HashMap<*, *>)
                }

                getRecipeInfoFromSavedRecipes()

            }

    }

    fun getRecipeInfoFromSavedRecipes() {


        //Per cada recipeId

        //Agafar de EdamamRecipes o de UsersPublicsRecipes

        //Guardar-ho


        val firebaseInstance = FirebaseDatabase.getInstance().reference.root

        val list = recipesXAnnotations.keys.toMutableStateList()

        for (i in 0 until recipesXAnnotations.size){

            //Si la recepta es de Edamam (comença per recipe_)

            if(list[i].toString().startsWith("recipe_")){

                firebaseInstance
                    .child("EdamamRecipes")
                    .child(list[i].toString())
                    .get()
                    .addOnCompleteListener {

                        val recipe = RecipeCustom()
                        if(it.result.value != null){
                            recipe.parseRecipeCustom(it.result.value as HashMap<Any, Any>)
                            llistaSavedRecipes.value.add(recipe)
                        }





                    }



            }
            else{
                //(Comença per foodieWeekly_)

            }
        }





    }


    fun getRecipesSaves(uri : String) {

        var firebase = FirebaseDatabase.getInstance().reference.root

        firebase.child("EdamamRecipes").child(uri).get()
            .addOnCompleteListener {
                if(it.result.exists()){
                     firebase.child("EdamamRecipes").child(uri).child("saves").get().addOnCompleteListener {
                         if(it.result.value != null && it.result.value != ""){
                             selectedRecipeSaves.value = it.result.value.toString().toInt()
                         }

                     }
                }
                else{
                    selectedRecipeSaves.value = 0
                }
            }
    }

    fun removeRecipeFromSavedRecipes(recipe : Any){
        var firebase = FirebaseDatabase.getInstance().reference.root

        //quitar de savedRecipes

        try{
            if(recipe is Recipe){
                var actualRecipe = recipe

                //Eliminar de llista userSavedRecipes



                recipesXAnnotations.remove(actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                ))

                llistaSavedRecipes.value.removeAt(getIndexOfRecipeCustomUri(actualRecipe.uri))

                //Actualitzar aquesta llista al firebase

                firebase
                    .child("Users")
                    .child(vm.authenticator.currentUID.value.toString())
                    .child("savedRecipes")
                    .setValue(recipesXAnnotations)




                //Actualitzar saves numero

                firebase
                    .child("EdamamRecipes")
                    .child(
                        actualRecipe.uri.replace(
                            "http://www.edamam.com/ontologies/edamam.owl#",
                            ""
                        )
                    )
                    .child("saves")
                    .get()
                    .addOnCompleteListener {

                        if(it.result.value != null){
                            val saved = it.result.value
                                .toString()
                                .toInt() - 1
                            Log.d("savedRecipes", "into edamam")
                            if (saved <= 0) {

                                //Si ningú te guardada la recepta, l'eliminem
                                firebase
                                    .child("EdamamRecipes")
                                    .child(
                                        actualRecipe.uri.replace(
                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                            ""
                                        )
                                    )
                                    .removeValue()

                                removeImageFromStorage(actualRecipe.uri.replace(
                                    "http://www.edamam.com/ontologies/edamam.owl#",
                                    ""
                                ))
                                Log.d(
                                    "savedRecipes",
                                    "quitado recipe de firebase"
                                )
                            }
                            else {

                                //Sinó, actualitzem el valor
                                firebase
                                    .child("EdamamRecipes")
                                    .child(
                                        actualRecipe.uri.replace(
                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                            ""
                                        )
                                    )
                                    .child("saves")
                                    .setValue(saved)


                            }

                            Log.d("savedRecipes", "final quitado")
                        }


                    }
            }
            else{
                var actualRecipe = recipe as RecipeCustom

                //Eliminar de llista userSavedRecipes

                recipesXAnnotations.remove(actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                ))

                llistaSavedRecipes.value.removeAt(getIndexOfRecipeCustomUri(actualRecipe.uri))

                //Actualitzar aquesta llista al firebase

                firebase
                    .child("Users")
                    .child(vm.authenticator.currentUID.value.toString())
                    .child("savedRecipes")
                    .setValue(recipesXAnnotations)




                //Actualitzar saves numero

                firebase
                    .child("EdamamRecipes")
                    .child(
                        actualRecipe.uri.replace(
                            "http://www.edamam.com/ontologies/edamam.owl#",
                            ""
                        )
                    )
                    .child("saves")
                    .get()
                    .addOnCompleteListener {
                        val saved = it.result.value
                            .toString()
                            .toInt() - 1
                        Log.d("savedRecipes", "into edamam")
                        if (saved <= 0) {

                            //Si ningú te guardada la recepta, l'eliminem
                            firebase
                                .child("EdamamRecipes")
                                .child(
                                    actualRecipe.uri.replace(
                                        "http://www.edamam.com/ontologies/edamam.owl#",
                                        ""
                                    )
                                )
                                .removeValue()
                            removeImageFromStorage(actualRecipe.uri.replace(
                                "http://www.edamam.com/ontologies/edamam.owl#",
                                ""
                            ), actualRecipe.username == "Edamam")

                            Log.d(
                                "savedRecipes",
                                "quitado recipe de firebase"
                            )
                        }
                        else {

                            //Sinó, actualitzem el valor
                            firebase
                                .child("EdamamRecipes")
                                .child(
                                    actualRecipe.uri.replace(
                                        "http://www.edamam.com/ontologies/edamam.owl#",
                                        ""
                                    )
                                )
                                .child("saves")
                                .setValue(saved)


                        }

                        Log.d("savedRecipes", "final quitado")

                    }
            }
        }
        catch(e : Exception){
            Log.d("removeRecipeFromSavedRecipes", e.message.toString())
        }

    }

    fun addRecipeToSavedRecipes(recipe: Any, annotation : String){
        Log.d("savedRecipes", "meterle savedRecipes")


        try{
            if(recipe is Recipe){
                val actualRecipe = recipe

                //Afegim uri a llista de receptes guardades


                recipesXAnnotations.put(
                    actualRecipe.uri.replace(
                        "http://www.edamam.com/ontologies/edamam.owl#",
                        ""),

                    annotation

                )

                addSavedRecipeCustom(recipe)

                val firebase = FirebaseDatabase.getInstance().reference.root

                //Guardem la nova llista
                firebase
                    .child("Users")
                    .child(vm.authenticator.currentUID.value.toString())
                    .child("savedRecipes")
                    .setValue(recipesXAnnotations)




                Log.d("savedRecipes", "recipe Parsed")

                //Compovem si aquesta recepta ja esta guardada, sino la afegim
                firebase
                    .child("EdamamRecipes")
                    .child(
                        actualRecipe.uri.replace(
                            "http://www.edamam.com/ontologies/edamam.owl#",
                            ""
                        )
                    )
                    .get()
                    .addOnCompleteListener {
                        if (!it.result.exists()) {

                            val newRecipe = RecipeCustom()
                            newRecipe.parseRecipe(actualRecipe)


                            addImageToStorage(actualRecipe, newRecipe)

                        }
                        else {
                            firebase
                                .child("EdamamRecipes")
                                .child(
                                    actualRecipe.uri.replace(
                                        "http://www.edamam.com/ontologies/edamam.owl#",
                                        ""
                                    )
                                )
                                .child("saves")
                                .get()
                                .addOnCompleteListener {
                                    if (it.result.value != null && it.result.value != "") {
                                        val saves = it.result.value
                                            .toString()
                                            .toInt()

                                        firebase
                                            .child("EdamamRecipes")
                                            .child(
                                                actualRecipe.uri.replace(
                                                    "http://www.edamam.com/ontologies/edamam.owl#",
                                                    ""
                                                )
                                            )
                                            .child("saves")
                                            .setValue(saves + 1)
                                    }
                                }
                        }
                    }
            }
            else{
                val actualRecipe = recipe as RecipeCustom

                //Afegim uri a llista de receptes guardades
                recipesXAnnotations.put(actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                ), annotation)

                addSavedRecipeCustom(actualRecipe)

                val firebase = FirebaseDatabase.getInstance().reference.root

                //Guardem la nova llista
                firebase
                    .child("Users")
                    .child(vm.authenticator.currentUID.value.toString())
                    .child("savedRecipes")
                    .setValue(recipesXAnnotations)




                Log.d("savedRecipes", "recipe Parsed")


            }
        }
        catch(e : Exception){
            Log.d("addRecipeToSavedRecipes", e.message.toString())
        }



    }


    fun getIndexOfRecipeCustomUri(uri : String) : Int{
        var found = false
        var i = 0

        while (!found && i < llistaSavedRecipes.value.size -1){
            if(llistaSavedRecipes.value[i].uri == uri)
            {
                found = true
            }
            else{
                i++;
            }
        }

        return i;
    }

    fun addSavedRecipeCustom(recipe : Any){

        if(recipe is Recipe){
            val newRecipeCustom = RecipeCustom()
            newRecipeCustom.parseRecipe(recipe)

            llistaSavedRecipes.value.add(newRecipeCustom)
        }
        else{
            llistaSavedRecipes.value.add(recipe as RecipeCustom)
        }



    }


    fun addImageToStorage(recipeEdamam : Recipe, customRecipe : RecipeCustom) {
        viewModelScope.launch {

            try {

                val image =
                    if(recipeEdamam.images.lARGE != null) recipeEdamam.images.lARGE.url
                    else if(recipeEdamam.images.rEGULAR != null) recipeEdamam.images.rEGULAR.url
                    else recipeEdamam.images.sMALL.url




                val imageLoader = ImageLoader(vm.context)
                val request = ImageRequest.Builder(vm.context)
                    .data(image)
                    .build()

                val bitmap = imageLoader.execute(request).drawable?.toBitmap()


                val storageRef =
                    FirebaseStorage.getInstance().reference.child("EdamamRecipesImages/" + recipeEdamam.uri
                        .replace(
                            "http://www.edamam.com/ontologies/edamam.owl#",
                            ""
                        ))

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val uploadTask = storageRef.putBytes(data)
                uploadTask.addOnSuccessListener {
                    // Image uploaded successfully
                    Log.d("successsssss", "TOMA YA PUTOS")

                    storageRef.downloadUrl.addOnCompleteListener {
                        customRecipe.imageUrl = it.result.toString()

                        FirebaseDatabase.getInstance().reference.root
                            .child("EdamamRecipes")
                            .child(
                                recipeEdamam.uri.replace(
                                    "http://www.edamam.com/ontologies/edamam.owl#",
                                    ""
                                )
                            )
                            .setValue(customRecipe)
                    }



                }.addOnFailureListener { exception ->
                    // Handle any errors
                    Log.d("successsssss", exception.message.toString())

            }
            } catch (e: Exception) {
                Log.d("dhjdhfjd", e.message.toString())
            }

        }
    }

    fun removeImageFromStorage(recipeUri : String, edamamRecipe : Boolean = true){

        viewModelScope.launch{
            if(edamamRecipe){
                val storageRef = FirebaseStorage.getInstance().reference.child("EdamamRecipesImages/$recipeUri")

                storageRef.delete()
            }
        }



    }

    fun addRecipeToCalendar(servings : MutableState<String>, recipe : Any, showDialog : MutableState<Boolean>) {
        Log.d("ShowAlertToAddRecipe adding ", "added recipe with -> " + servings.value)
        var firebase = FirebaseDatabase.getInstance().reference.root
        firebase
            .child("Weeks")
            .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
            .child("days")
            .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
            .child("meals")
            .child(vm.recipesViewModel.selectedMeal.name.lowercase())
            .get()
            .addOnCompleteListener {

                if(recipe is Recipe){
                    if(it.result.exists()) {

                        Log.d("ShowAlertToAddRecipe", "it exists")
                        Log.d("ShowAlertToAddRecipe", vm.recipesViewModel.selectedMeal.name.lowercase())


                        var recipesFromMeal = it.result.value as HashMap<Any, Any>

                        //Check if recipe is already on savedRecipes

                        firebase
                            .child("EdamamRecipes")
                            .child(recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))
                            .get()
                            .addOnCompleteListener {
                                //If recipe already added on EdamamRecipes, no need to add it. If not, add to it
                                if(!it.result.exists()){
                                    val newRecipe = RecipeCustom()
                                    newRecipe.parseRecipe(recipe)
                                    recipesFromMeal.put(recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""),servings.value)

                                    addImageToStorage(recipe, newRecipe)

                                    firebase
                                        .child("Weeks")
                                        .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                                        .child("days")
                                        .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                                        .child("meals")
                                        .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                                        .setValue(recipesFromMeal)
                                        .addOnCompleteListener {
                                            Log.d("ShowAlertToAddRecipe", "done adding")


                                            addRecipeToMealsInDay(newRecipe, servings.value.toInt())


                                        }
                                }
                                else{

                                    val newRep = RecipeCustom()
                                    newRep.parseRecipeCustom(it.result.value as HashMap<Any, Any>)
                                    recipesFromMeal.put(newRep.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""),servings.value)

                                    addRecipeToMealsInDay(newRep, servings.value.toInt())
                                    firebase
                                        .child("Weeks")
                                        .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                                        .child("days")
                                        .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                                        .child("meals")
                                        .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                                        .setValue(recipesFromMeal).addOnCompleteListener {

                                            Log.d("ShowAlertToAddRecipe", "done adding")
                                        }
                                }
                            }

                    }
                    else{
                        Log.d("ShowAlertToAddRecipe", "meals doesnt exist")

                        var recipesFromMeal = hashMapOf<String, String>()
                        recipesFromMeal.put(
                            recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""),
                            servings.value)


                        //Check if recipe is already on savedRecipes

                        firebase
                            .child("EdamamRecipes")
                            .child(recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))
                            .get()
                            .addOnCompleteListener {
                                //If recipe already added on EdamamRecipes, no need to add it. If not, add to it
                                if(!it.result.exists()){
                                    val newRecipe = RecipeCustom()
                                    newRecipe.parseRecipe(recipe)


                                    addImageToStorage(recipe, newRecipe)

                                    firebase
                                        .child("Weeks")
                                        .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                                        .child("days")
                                        .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                                        .child("meals")
                                        .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                                        .setValue(recipesFromMeal)
                                        .addOnCompleteListener {
                                            Log.d("ShowAlertToAddRecipe", "done adding")


                                            addRecipeToMealsInDay(newRecipe, servings.value.toInt())


                                        }
                                }
                                else{

                                    val newRep = RecipeCustom()
                                    newRep.parseRecipeCustom(it.result.value as HashMap<Any, Any>)
                                    addRecipeToMealsInDay(newRep, servings.value.toInt())
                                    firebase
                                        .child("Weeks")
                                        .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                                        .child("days")
                                        .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                                        .child("meals")
                                        .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                                        .setValue(recipesFromMeal)
                                        .addOnCompleteListener {
                                            Log.d("ShowAlertToAddRecipe", "done adding")
                                        }
                                }
                            }
                    }
                }
                else if(recipe is RecipeCustom){
                    Log.d("ShowAlertToAddRecipe", "i am custom recipe")
                    if(it.result.exists()) {
                        Log.d("ShowAlertToAddRecipe", "it exists")
                        Log.d("ShowAlertToAddRecipe", vm.recipesViewModel.selectedMeal.name.lowercase())

                        //Add to day recipes map
                        var recipesFromMeal = it.result.value as HashMap<Any, Any>
                        recipesFromMeal.put(recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""),servings.value )


                        firebase
                            .child("Weeks")
                            .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                            .child("days")
                            .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                            .child("meals")
                            .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                            .setValue(recipesFromMeal)
                            .addOnCompleteListener {
                                Log.d("ShowAlertToAddRecipe", "done adding")

                                firebase
                                    .child("Weeks")
                                    .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                                    .child("days")
                                    .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                                    .child("meals")
                                    .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                                    .child(recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))
                                    .get()
                                    .addOnCompleteListener{
                                        if(it.result.exists()){
                                            addRecipeToMealsInDay(recipe, servings.value.toInt())
                                        }
                                    }

                            }


                    }
                    else{
                        Log.d("ShowAlertToAddRecipe", "meals doesnt exist")

                        var recipesFromMeal = hashMapOf<String, String>()
                        recipesFromMeal.put(
                            recipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""),
                            servings.value)


                        firebase
                            .child("Weeks")
                            .child(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value.toString())
                            .child("days")
                            .child(vm.pantallaPrincipalViewModel.selectedDayIndex.value.toString())
                            .child("meals")
                            .child(vm.recipesViewModel.selectedMeal.name.lowercase())
                            .setValue(recipesFromMeal).addOnCompleteListener {

                                addRecipeToMealsInDay(recipe, servings.value.toInt())
                            }
                    }
                }

                vm.shoppingViewModel.addIngredientsToShoppingList(recipe, servings.value.toInt())


                showDialog.value = false
                /*vm.pantallaPrincipalViewModel.getMealsFromDay(vm.pantallaPrincipalViewModel.selectedIndexCalendarWeekId.value,
                    vm.pantallaPrincipalViewModel.selectedDayIndex.value)*/
                vm.navController.navigate(Destinations.PantallaPrincipal.ruta){
                    popUpTo(Destinations.RecipesScreen.ruta)
                }
            }






    }





    fun addRecipeToMealsInDay(recipe : RecipeCustom, servings : Int){
        var i = -1
        when(vm.recipesViewModel.selectedMeal.name.lowercase()){
            "breakfast" -> i = 0
            "lunch" -> i = 1
            "dinner" -> i = 2
            "snack" -> i = 3
        }

        Log.d("addRecipeToMealsInDay", "added "+ recipe.label)
        if(!vm.pantallaPrincipalViewModel.weekMealsList.value[vm.pantallaPrincipalViewModel.selectedDayIndex.value][i].contains(recipe)){
            vm.pantallaPrincipalViewModel.weekMealsList.value[vm.pantallaPrincipalViewModel.selectedDayIndex.value][i].put(recipe, servings)
        }
        else{
            vm.pantallaPrincipalViewModel.weekMealsList.value[vm.pantallaPrincipalViewModel.selectedDayIndex.value][i][recipe] = servings
        }

    }
}