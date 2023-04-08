package com.example.foodieweekly_appv2.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieweekly_appv2.model.recipesApi.*
import com.example.foodieweekly_appv2.xarxa.RecipesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecipesViewModel : ViewModel() {

    /*private var _respostaRecipes = mutableStateOf(Recipes(, null))

    val respostaRecipes = _respostaRecipes;*/

    private var _respostaRecipes = mutableStateOf(Recipes(0, 0, emptyList(), LinksX(Next("", "")), 0))

    val respostaRecipes = _respostaRecipes;

    private var _llistaRecipes : MutableState<List<Hit>> =  mutableStateOf(emptyList())

    val llistaRecipes = _llistaRecipes

    fun get() {
        viewModelScope.launch(Dispatchers.IO){

            try{

                val resultat = RecipesClient.servei
                    .getRecipesOf(RecipesClient.APP_KEY,
                        RecipesClient.APP_ID,"oat","public")

                respostaRecipes.value = resultat
                llistaRecipes.value = respostaRecipes.value.hits

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