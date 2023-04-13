package com.example.foodieweekly_appv2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.model.enums.TypeOfSingup
import com.example.foodieweekly_appv2.navigation.Destinations

class MainViewModel : ViewModel() {

    var loginViewModel = LoginViewModel();
    var signupViewModel = SignupViewModel();
    var pantallaPrincipalViewModel = PantallaPrincipalViewModel();
    var recipesViewModel = RecipesViewModel()

}