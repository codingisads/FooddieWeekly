package com.example.foodieweekly_appv2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.vm
import com.google.firebase.database.FirebaseDatabase

class ShoppingViewModel : ViewModel() {

    private var _usersShoppingList = mutableStateOf<HashMap<String, String>>(hashMapOf())
    val usersShoppingList = _usersShoppingList


    fun addIngredientsToShoppingList(recipe : Any, servings : Int){

        var customRecipe = RecipeCustom()

        if(recipe is Recipe)
            customRecipe.parseRecipe(recipe)
        else
            customRecipe = recipe as RecipeCustom


        for (i in 0 until customRecipe.ingredientsNameList.size){
            if(usersShoppingList.value.containsKey(customRecipe.ingredientsNameList[i])){
                var ingredientQuantityMeasure = usersShoppingList.value[customRecipe.ingredientsNameList[i]]
                var quantityUnitArr = ingredientQuantityMeasure!!.split(" ")

                var newQuantity = customRecipe.ingredientsQuantityList[i] * servings

                var newQuantityUnit = newQuantity.toString() + " " + quantityUnitArr[1]

                usersShoppingList.value[customRecipe.ingredientsNameList[i]] = newQuantityUnit
            }
            else{
                usersShoppingList.value.put(customRecipe.ingredientsNameList[i],
                    customRecipe.ingredientsQuantityList[i].toString() + " " + customRecipe.ingredientsMeasureList[i])
            }
        }

        addShoppingListToFirebase()


    }

    fun addShoppingListToFirebase(){
        var db = FirebaseDatabase.getInstance().reference.root

        db
            .child("Users")
            .child(vm.authenticator.currentUID.value)
            .child("shoppingList")
            .setValue(usersShoppingList.value)
    }

    fun parseShoppingListFromFirebase(shoppingList : HashMap<String, String>){
        usersShoppingList.value = shoppingList
    }

}