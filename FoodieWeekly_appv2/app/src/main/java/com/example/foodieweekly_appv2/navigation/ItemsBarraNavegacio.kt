package com.example.foodieweekly_appv2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow

object ItemsBarraNavegacio {
    val Items = listOf(
        ItemBarraNavegacio("Login",
            Icons.Filled.Home,
            Destinations.Login.ruta),
        ItemBarraNavegacio("Signup",
            Icons.Filled.CheckCircle,
            Destinations.Signup.ruta),
        ItemBarraNavegacio("Main",
            Icons.Filled.PlayArrow,
            Destinations.PantallaPrincipal.ruta)
    )
}