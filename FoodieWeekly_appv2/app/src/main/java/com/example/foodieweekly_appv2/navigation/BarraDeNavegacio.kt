package com.example.foodieweekly_appv2.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foodieweekly_appv2.Main
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.viewmodel.MainViewModel
import com.example.foodieweekly_appv2.vm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalBarraDeNavegacio(
    vm: MainViewModel,
    navController: NavHostController,
    estatDrawer: DrawerState,
    scope: CoroutineScope
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var showBar = remember { mutableStateOf(false)}

    showBar.value = navBackStackEntry?.destination?.route == "PantallaPrincipal" ||
            navBackStackEntry?.destination?.route == "RecipesScreen" ||
            navBackStackEntry?.destination?.route == "ShoppingList"

    Scaffold (
        topBar = {BarraDeTitol(navController, estatDrawer, scope)},
        bottomBar = {if(showBar.value) {
            BarraDeNavegacio(navController)
        }},
        content = { valorsPadding -> Main(vm, valorsPadding)}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraDeTitol(navController : NavHostController, estatDrawer: DrawerState, scope: CoroutineScope){
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    if(navBackStackEntry?.destination?.route == "PantallaPrincipal"){
        TopAppBar(
            title = {},
            navigationIcon = {
                Icon(
                    Icons.Outlined.Menu,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            scope.launch {
                                estatDrawer.open()
                            }
                        },
                    contentDescription = "drawable icons"
                )

            }
        )
    }

}

/*
*
* Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){


                    Icon(
                        Icons.Outlined.AccountCircle,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                            },
                        contentDescription = "drawable icons"

                    )
                }

            }*/

@Composable
fun BarraDeNavegacio(navController: NavHostController) {
    val backStateEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        ItemsBarraNavegacio.Items.forEach{
            NavigationBarItem(selected = it.ruta == backStateEntry?.destination?.route,
                onClick = {
                    if(it.ruta == "RecipesScreen"){
                        vm.recipesViewModel.addMode.value = false;
                    }
                    navController.navigate(it.ruta){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    },
                icon = {Icon(painterResource(it.imatge), "Navegacio")},
                label = { Text(it.titol, fontFamily = Poppins, fontSize = 12.sp)}
            )
        }
    }
}