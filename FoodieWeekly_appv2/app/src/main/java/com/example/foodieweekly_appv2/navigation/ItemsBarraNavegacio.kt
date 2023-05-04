package com.example.foodieweekly_appv2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow

object ItemsBarraNavegacio {
    val Items = listOf(
        ItemBarraNavegacio("Calendar",
            Icons.Filled.Home,
            Destinations.PantallaPrincipal.ruta),
        ItemBarraNavegacio("Recipes",
            Icons.Filled.CheckCircle,
            Destinations.RecipesScreen.ruta),
        ItemBarraNavegacio("Shop List",
            Icons.Filled.PlayArrow,
            Destinations.ShoppingList.ruta)
    )
}