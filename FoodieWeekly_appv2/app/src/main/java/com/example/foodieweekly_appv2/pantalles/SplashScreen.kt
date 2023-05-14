package com.example.foodieweekly_appv2.pantalles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.vm
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {

    LaunchedEffect(key1 = true){
        delay(2000)
        vm.navController.navigate(Destinations.Login.ruta)
    }

    Splash()
}

@Composable
fun Splash(){

    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, 
        verticalArrangement = Arrangement.Center){

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Image(painter = painterResource(R.drawable.background), contentDescription = "logoSplashScreen",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize())

            Image(painter = painterResource(R.drawable.logo), contentDescription = "logoSplashScreen",
                modifier = Modifier.padding(60.dp))
        }
        

        
    }

}