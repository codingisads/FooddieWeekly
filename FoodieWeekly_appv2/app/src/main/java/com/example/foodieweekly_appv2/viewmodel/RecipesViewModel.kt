package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieweekly_appv2.model.recipesApi.*
import com.example.foodieweekly_appv2.xarxa.RecipesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {

    /*private var _respostaRecipes = mutableStateOf(Recipes(, null))

    val respostaRecipes = _respostaRecipes;*/

    private var _respostaRecipes = mutableStateOf(Recipes(0, 0, emptyList(), LinksX(Next("", "")), 0))
    val respostaRecipes = _respostaRecipes;

    private var _llistaRecipes : MutableState<MutableList<Hit>> =  mutableStateOf(mutableListOf())
    val llistaRecipes = _llistaRecipes

    private var _nextPageLink = mutableStateOf("")
    public val nextPageLink = _nextPageLink

    lateinit var selectedRecipe : Recipe

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


    fun setActualRecipe(rec : Recipe)  {
        selectedRecipe = rec
    }

    fun getActualRecipe() : Recipe {
        return selectedRecipe
    }
}