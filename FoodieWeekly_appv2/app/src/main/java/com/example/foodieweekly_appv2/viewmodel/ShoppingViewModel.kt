package com.example.foodieweekly_appv2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ShoppingViewModel : ViewModel() {

    private var _usersShoppingList = mutableStateOf<HashMap<String, Int>>(hashMapOf())
    val usersShoppingList = _usersShoppingList






}