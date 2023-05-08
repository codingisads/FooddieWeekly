package com.example.foodieweekly_appv2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foodieweekly_appv2.Main
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.viewmodel.MainViewModel
import com.example.foodieweekly_appv2.vm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalBarraDeNavegacio(
    vm: MainViewModel,
    navController: NavHostController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var showBar = remember { mutableStateOf(false)}

    showBar.value = navBackStackEntry?.destination?.route == "PantallaPrincipal" ||
            navBackStackEntry?.destination?.route == "RecipesScreen" ||
            navBackStackEntry?.destination?.route == "ShoppingList"

    Scaffold (
        //topBar = {BarraDeTitol(navController)},
        bottomBar = {if(showBar.value) {
            BarraDeNavegacio(navController)
        }},
        content = { valorsPadding -> Main(vm, valorsPadding)}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraDeTitol(navController : NavHostController){
    TopAppBar(
        title = { Text("Prova") },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp()}) {
                Icon(Icons.Filled.ArrowBack, "Navegacio")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Composable
fun BarraDeNavegacio(navController: NavHostController) {
    val backStateEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        ItemsBarraNavegacio.Items.forEach{
            NavigationBarItem(selected = it.ruta == backStateEntry?.destination?.route,
                onClick = {
                    if(it.ruta == "PantallaPrincipal"){
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