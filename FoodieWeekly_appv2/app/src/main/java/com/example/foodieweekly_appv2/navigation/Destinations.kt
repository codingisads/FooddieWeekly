package com.example.foodieweekly_appv2.navigation

sealed class Destinations (val ruta:String){
    object Login: Destinations("Login")
    object Signup: Destinations("Signup")
    object SignupConfig: Destinations("SignupConfig")
    object SignupUserBodyConfig: Destinations("SignupUserBodyConfig")
    object SignupUserDiet: Destinations("SignupUserDiet")
    object SignupUserPreferences: Destinations("SignupUserPreferences")
    object SignupLastScreen: Destinations("SignupLastScreen")
    object PantallaPrincipal: Destinations("PantallaPrincipal")
    object RecipesScreen: Destinations("RecipesScreen")


}