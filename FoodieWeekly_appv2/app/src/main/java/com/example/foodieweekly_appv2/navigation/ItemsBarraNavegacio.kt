package com.example.foodieweekly_appv2.navigation

import com.example.foodieweekly_appv2.R

object ItemsBarraNavegacio {
    val Items = listOf(
        ItemBarraNavegacio("Calendar",
            R.drawable.calendar_logo,
            Destinations.PantallaPrincipal.ruta),
        ItemBarraNavegacio("Recipes",
            R.drawable.recipes_logo,
            Destinations.RecipesScreen.ruta),
        ItemBarraNavegacio("Shop List",
            R.drawable.shopping_logo,
            Destinations.ShoppingList.ruta)
    )
}