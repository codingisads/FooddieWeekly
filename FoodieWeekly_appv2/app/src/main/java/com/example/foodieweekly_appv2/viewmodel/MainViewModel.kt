package com.example.foodieweekly_appv2.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.firebase.Authenticator

class MainViewModel : ViewModel() {

    var loginViewModel = LoginViewModel();
    var signupViewModel = SignupViewModel();
    var pantallaPrincipalViewModel = PantallaPrincipalViewModel();
    var recipesViewModel = RecipesViewModel()

    lateinit var navController : NavHostController

    lateinit var authenticator: Authenticator

    lateinit var context : Context

    lateinit var activity : Activity



}