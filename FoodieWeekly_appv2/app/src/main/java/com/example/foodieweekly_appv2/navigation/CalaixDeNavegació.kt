package com.example.foodieweekly_appv2.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.vm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalCalaixDeNavegacio(navController: NavHostController) {
    val estatDrawer = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var rutaActual =  remember{ mutableStateOf(Destinations.PantallaPrincipal.ruta) }


    navController.addOnDestinationChangedListener(
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            rutaActual.value = controller.currentDestination?.route?:Destinations.PantallaPrincipal.ruta
        }
    )

    ModalNavigationDrawer(
        drawerState = estatDrawer,
        gesturesEnabled = rutaActual.value == Destinations.PantallaPrincipal.ruta,
        drawerContent = {
            ModalDrawerSheet(){

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your calendars",
                        Modifier.padding(top = 30.dp, start = 10.dp),
                    fontFamily = Poppins,
                    fontSize = 22.sp, textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold)
                }


            }
        },
        content = {
            PrincipalBarraDeNavegacio(vm = vm, navController = navController, estatDrawer, scope)
        })

}