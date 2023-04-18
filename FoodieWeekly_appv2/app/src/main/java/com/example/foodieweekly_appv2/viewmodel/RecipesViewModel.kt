package com.example.foodieweekly_appv2.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.model.recipesApi.LinksX
import com.example.foodieweekly_appv2.model.recipesApi.Next
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.model.recipesApi.Recipes
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
    val respostaRecipes = _respostaRecipes;

    private var _nextPageLink = mutableStateOf("")
    public val nextPageLink = _nextPageLink

    private var _llistaRecipes : MutableState<MutableList<Any>> =mutableStateOf(mutableListOf())
    val llistaRecipes = _llistaRecipes

    private var _llistaSavedRecipes : MutableState<MutableList<RecipeCustom>> =mutableStateOf(mutableListOf())
    val llistaSavedRecipes = _llistaSavedRecipes


    public var userSavedRecipesIds = arrayListOf<Any>()

    lateinit var selectedRecipe : Any

    var selectedRecipeSaves = mutableStateOf(0)



    private var _addMode = mutableStateOf(false)
    public val addMode = _addMode


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

    fun getNextPage() {
        viewModelScope.launch(Dispatchers.IO){

            try{

                val resultat = RecipesClient.servei
                    .getNextPage(nextPageLink.value)

                respostaRecipes.value = resultat

                nextPageLink.value = respostaRecipes.value.links.next.href

                Log.d("getRecipes recipes page", nextPageLink.value)

                llistaRecipes.value.addAll(respostaRecipes.value.hits)

                Log.d("getRecipes page", "done")
                //llistaRecipes.value = respostaRecipes.value.results
            }
            catch(e : Exception){
                Log.d("getRecipes page", e.message.toString())
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
                    userSavedRecipesIds = result as ArrayList<Any>
                }

                getRecipeInfoFromSavedRecipes()

            }

    }

    fun getRecipeInfoFromSavedRecipes() {


        //Per cada recipeId

        //Agafar de EdamamRecipes o de UsersPublicsRecipes

        //Guardar-ho


        val firebaseInstance = FirebaseDatabase.getInstance().reference.root


        for (i in 0 until userSavedRecipesIds.size){

            //Si la recepta es de Edamam (comença per recipe_)
            if(userSavedRecipesIds[i].toString().startsWith("recipe_")){

                firebaseInstance
                    .child("EdamamRecipes")
                    .child(userSavedRecipesIds[i].toString())
                    .get()
                    .addOnCompleteListener {

                        val recipe = RecipeCustom()
                        recipe.parseRecipeCustom(it.result.value as HashMap<Any, Any>)


                        llistaSavedRecipes.value.add(recipe)

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


        if(recipe is Recipe){
            var actualRecipe = recipe

            //Eliminar de llista userSavedRecipes

            userSavedRecipesIds.remove(
                actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                )
            )

            llistaSavedRecipes.value.removeAt(getIndexOfRecipeCustomUri(actualRecipe.uri))

            //Actualitzar aquesta llista al firebase

            firebase
                .child("Users")
                .child(vm.authenticator.currentUID.value.toString())
                .child("savedRecipes")
                .setValue(userSavedRecipesIds)




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
        else{
            var actualRecipe = recipe as RecipeCustom

            //Eliminar de llista userSavedRecipes

            userSavedRecipesIds.remove(
                actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                )
            )

            llistaSavedRecipes.value.removeAt(getIndexOfRecipeCustomUri(actualRecipe.uri))

            //Actualitzar aquesta llista al firebase

            firebase
                .child("Users")
                .child(vm.authenticator.currentUID.value.toString())
                .child("savedRecipes")
                .setValue(userSavedRecipesIds)




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

    fun addRecipeToSavedRecipes(recipe: Any){
        Log.d("savedRecipes", "meterle savedRecipes")


        if(recipe is Recipe){
            val actualRecipe = recipe

            //Afegim uri a llista de receptes guardades
            vm.recipesViewModel.userSavedRecipesIds.add(
                actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                )
            )

            addSavedRecipeCustom(recipe)

            val firebase = FirebaseDatabase.getInstance().reference.root

            //Guardem la nova llista
            firebase
                .child("Users")
                .child(vm.authenticator.currentUID.value.toString())
                .child("savedRecipes")
                .setValue(vm.recipesViewModel.userSavedRecipesIds)




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


                        addImageToStorage(newRecipe.imageUrl, newRecipe.uri, newRecipe.username == "Edamam")





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
            vm.recipesViewModel.userSavedRecipesIds.add(
                actualRecipe.uri.replace(
                    "http://www.edamam.com/ontologies/edamam.owl#",
                    ""
                )
            )

            addSavedRecipeCustom(actualRecipe)

            val firebase = FirebaseDatabase.getInstance().reference.root

            //Guardem la nova llista
            firebase
                .child("Users")
                .child(vm.authenticator.currentUID.value.toString())
                .child("savedRecipes")
                .setValue(vm.recipesViewModel.userSavedRecipesIds)




            Log.d("savedRecipes", "recipe Parsed")


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


    fun addImageToStorage(recipe : Any, edamamRecipe : Boolean = true) {
        viewModelScope.launch {

            try {
                if (edamamRecipe) {
                    val imageLoader = ImageLoader(vm.context)
                    val request = ImageRequest.Builder(vm.context)
                        .data(imageUrl)
                        .build()

                    val bitmap = imageLoader.execute(request).drawable?.toBitmap()


                    val storageRef =
                        FirebaseStorage.getInstance().reference.child("EdamamRecipesImages/" + recipeUri)

                    val baos = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnSuccessListener {
                        // Image uploaded successfully
                        Log.d("successsssss", "TOMA YA PUTOS")

                        FirebaseDatabase.getInstance().reference.root
                            .child("EdamamRecipes")
                            .child(
                                recipeUri.replace(
                                    "http://www.edamam.com/ontologies/edamam.owl#",
                                    ""
                                )
                            )
                            .setValue(newRecipe)
                    }.addOnFailureListener { exception ->
                        // Handle any errors
                        Log.d("successsssss", "TOMA YA PUTOS")
                    }
                } else {
                    val imageLoader = ImageLoader(vm.context)
                    val request = ImageRequest.Builder(vm.context)
                        .data(imageUrl)
                        .build()

                    val bitmap = imageLoader.execute(request).drawable?.toBitmap()


                    val storageRef =
                        FirebaseStorage.getInstance().reference.child("UsersPublicRecipesImages/" + recipeUri)

                    val baos = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnSuccessListener {
                        // Image uploaded successfully
                        Log.d("successsssss", "TOMA YA PUTOS")
                    }.addOnFailureListener { exception ->
                        // Handle any errors
                        Log.d("successsssss", "TOMA YA PUTOS")
                    }


                }
            } catch (e: Exception) {
                Log.d("dhjdhfjd", e.message.toString())
            }

        }
    }

    fun removeImageFromStorage(recipeUri : String, edamamRecipe : Boolean = true){

        viewModelScope.launch{
            if(edamamRecipe){
                val storageRef = FirebaseStorage.getInstance().reference.child("EdamamRecipes/$recipeUri")

                storageRef.delete()
            }
        }



    }
}