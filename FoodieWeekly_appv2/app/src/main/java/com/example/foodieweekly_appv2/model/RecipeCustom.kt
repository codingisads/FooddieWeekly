package com.example.foodieweekly_appv2.model

import com.example.foodieweekly_appv2.model.recipesApi.Recipe

class RecipeCustom {
    public var label : String = ""

    public var uri : String = ""
    public var saves : Int = 1
    public var time : Int = 0
    public var totalKcals : Int = 0
    public var kcalsPerServing : Int = 0
    public var imageUrl : String = "";
    var healthLabels : List<String> = listOf()
    var username : String = "Edamam"

    var servings : Int = 0

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
        this.time = recipe.totalTime.toInt()
        this.totalKcals = recipe.calories.toInt()
        this.kcalsPerServing = recipe.calories.toInt() / recipe.yield.toInt()

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

            ingredientsQuantityList.add(recipe.ingredients[i].quantity.toInt())
        }

        for (i in 0 until recipe.digest.size){
            nutritionLabels.add(recipe.digest[i].label)
            nutritionQuantity.add(recipe.digest[i].total.toInt())
            nutritionUnits.add(recipe.digest[i].unit)
        }

    }
}