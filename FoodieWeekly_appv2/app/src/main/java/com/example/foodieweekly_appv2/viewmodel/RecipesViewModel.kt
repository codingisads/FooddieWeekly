package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieweekly_appv2.model.recipesApi.Hit
import com.example.foodieweekly_appv2.model.recipesApi.LinksX
import com.example.foodieweekly_appv2.model.recipesApi.Next
import com.example.foodieweekly_appv2.model.recipesApi.Recipes
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

    fun get() {
        viewModelScope.launch(Dispatchers.IO){

            try{

                val resultat = RecipesClient.servei
                    .getRecipesOf(RecipesClient.APP_KEY,
                        RecipesClient.APP_ID,"oat","public")

                respostaRecipes.value = resultat
                llistaRecipes.value.addAll(respostaRecipes.value.hits)

                llistaRecipes.value.forEach{
                    Log.d("getRecipes recipes", it.recipe.label.toString())
                }

                Log.d("getRecipes", "done")
                //llistaRecipes.value = respostaRecipes.value.results
            }
            catch(e : Exception){
                Log.d("getRecipes", e.message.toString())
            }

        }


        Log.d("getRecipes returning", llistaRecipes.value.size.toString())

    }

}