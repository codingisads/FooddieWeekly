package com.example.foodieweekly_appv2.model

import android.util.Log
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import java.lang.Math.ceil

class RecipeCustom {
    public var label : String = ""

    var uri : String = ""
    var saves : Int = 1
    var time : Int = 0
    var totalKcals : Int = 0
    var kcalsPerServing : Int = 0
    var imageUrl : String = "";
    var username : String = "Edamam"
    var servings : Int = 0

    var steps : MutableList<String> = mutableListOf()



    var healthLabels : List<String> = listOf()

    var ingredientsNameList : MutableList<String> = mutableListOf()
    var ingredientsQuantityList : MutableList<Int> = mutableListOf()
    var ingredientsMeasureList : MutableList<String> = mutableListOf()

    var nutritionLabels : MutableList<String> = mutableListOf()
    var nutritionQuantity : MutableList<Int> = mutableListOf()
    var nutritionUnits : MutableList<String> = mutableListOf()

    init{

    }

    fun parseRecipe(recipe: Recipe){

        this.uri = recipe.uri

        this.label = recipe.label
        this.time = ceil(recipe.totalTime).toInt()
        this.totalKcals = ceil(recipe.calories).toInt()
        this.kcalsPerServing = ceil(recipe.calories).toInt() / recipe.yield.toInt()

        if(recipe.images.lARGE!= null){
            this.imageUrl =  recipe.images.lARGE.url
        }
        else if(recipe.images.rEGULAR!= null){
            this.imageUrl =  recipe.images.rEGULAR.url
        }
        else{
            this.imageUrl =  recipe.images.sMALL.url
        }



        this.healthLabels = recipe.healthLabels
        this.servings = recipe.yield.toInt()

        for (i in 0 until recipe.ingredients.size){
            ingredientsNameList.add(recipe.ingredients[i].food)
            if(recipe.ingredients[i].measure == null){
                ingredientsMeasureList.add("as pleased")
            }
            else{
                ingredientsMeasureList.add(recipe.ingredients[i].measure)
            }

            ingredientsQuantityList.add(ceil(recipe.ingredients[i].quantity).toInt())
        }

        for (i in 0 until recipe.digest.size){
            nutritionLabels.add(recipe.digest[i].label)
            nutritionQuantity.add(ceil(recipe.digest[i].total).toInt())
            nutritionUnits.add(recipe.digest[i].unit)
        }

    }


    fun parseRecipeCustom(recipe : HashMap<Any, Any>){
        this.label = recipe["label"] as String
        this.uri = recipe["uri"] as String

        var re =  recipe["saves"]
        this.saves = (recipe["saves"] as Long).toInt()


        this.time = (recipe["time"] as Long).toInt()
        this.totalKcals = (recipe["totalKcals"] as Long).toInt()
        this.kcalsPerServing = (recipe["kcalsPerServing"] as Long).toInt()
        this.imageUrl = recipe["imageUrl"] as String
        this.username = recipe["username"] as String
        this.servings = (recipe["servings"] as Long).toInt()

        this.healthLabels = recipe["healthLabels"] as List<String>

        this.ingredientsNameList = recipe["ingredientsNameList"] as MutableList<String>
        this.ingredientsMeasureList = recipe["ingredientsMeasureList"] as MutableList<String>
        this.ingredientsQuantityList = recipe["ingredientsQuantityList"] as MutableList<Int>


        this.nutritionLabels = recipe["nutritionLabels"] as MutableList<String>
        this.nutritionUnits = recipe["nutritionUnits"] as MutableList<String>
        this.nutritionQuantity = recipe["nutritionQuantity"] as MutableList<Int>

    }

    fun parseMapToNutritionTotal(recipes : MutableList<HashMap<RecipeCustom, Int>>){

        try{
            if(recipes.size > 0){

                var firstRecipe = recipes[0].keys.toList()
                if(firstRecipe.size > 0){
                    nutritionLabels = recipes[0].keys.toList()[0].nutritionLabels
                    nutritionUnits = recipes[0].keys.toList()[0].nutritionUnits
                    for (i in 0 until recipes.size){
                        val recipesList = recipes[i].keys.toList()
                        val servingsList = recipes[i].values.toList()

                        for (j in 0 until recipesList.size){

                            for (k in 0 until recipesList[j].nutritionQuantity.size){
                                nutritionQuantity.add(0)
                                nutritionQuantity[k] += (recipesList[j].nutritionQuantity[k].toDouble() / servingsList[j]).toInt()
                            }

                            totalKcals += (recipesList[j].kcalsPerServing * servingsList[j])
                        }
                    }
                }

            }
        }
        catch(e : Exception){
            Log.d("parseMapToNutritionTotal", e.message.toString())
        }


    }


}