package com.example.foodieweekly_appv2

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.model.enums.MealType
import com.example.foodieweekly_appv2.model.enums.TypeOfSingup
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.navigation.ItemsBarraNavegacio
import com.example.foodieweekly_appv2.pantalles.*
import com.example.foodieweekly_appv2.ui.theme.FoodieWeekly_appv2Theme
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.viewmodel.MainViewModel
import com.example.foodieweekly_appv2.xarxa.RecipesClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.foodieweekly_appv2.model.recipesApi.Hit
import com.example.foodieweekly_appv2.viewmodel.LoginViewModel
import com.example.foodieweekly_appv2.viewmodel.RecipesViewModel
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel

var _showSignupConfig =  mutableStateOf(false)
val showSignupConfig = _showSignupConfig
val alreadyInDb =  mutableStateOf(false)

val authenticator : Authenticator = Authenticator();
val vm = MainViewModel()

class MainActivity : ComponentActivity() {



    val activity = this@MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController : NavHostController = rememberNavController()


            FoodieWeekly_appv2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //val llistaRecipes : MutableState<List<Hit>> =  mutableStateOf(emptyList())


                    Main(vm, navController, activity, authenticator)
                    //RecipeElement()
                    //Total()
                    //PrincipalBarraDeNavegacio(navController)
                    //SignupUserBodyConfig()
                    //SignupLastScreen()
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("autenticacion google rc", requestCode.toString())
        if (requestCode == 1) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            vm.signupViewModel.signupType.value = TypeOfSingup.GOOGLE
            authenticator.finishLogin(task)


        }

    }
}

@Composable
fun Main(vm: MainViewModel, navController: NavHostController, activity: MainActivity, authenticator : Authenticator,
         paddingValues: PaddingValues = PaddingValues(0.dp)) {

    var db = RealtimeDatabase();
    NavHost(
        navController = navController,
        startDestination = Destinations.Login.ruta,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Destinations.Login.ruta)
        {
            Login(vm.loginViewModel, navController, authenticator, activity)
        }
        composable(Destinations.Signup.ruta) {
            Signup(vm.signupViewModel, navController, authenticator, activity)
        }
        composable(Destinations.SignupConfig.ruta) {
            SignupConfig(vm.signupViewModel, navController, authenticator)
        }
        composable(Destinations.SignupUserBodyConfig.ruta) {
            SignupUserBodyConfig(vm.signupViewModel, navController, authenticator)
        }
        composable(Destinations.SignupUserDiet.ruta) {
            SignupUserDiet(vm.signupViewModel, navController, authenticator)
        }
        composable(Destinations.SignupUserPreferences.ruta) {
            SignupUserPreferences(vm.signupViewModel, navController, authenticator)
        }
        composable(Destinations.SignupLastScreen.ruta) {
            SignupLastScreen(vm.signupViewModel, navController, authenticator, vm.signupViewModel)
        }
        composable(Destinations.PantallaPrincipal.ruta) {
            PantallaPrincipal(authenticator, vm.pantallaPrincipalViewModel, vm.recipesViewModel, navController)
        }
        composable(Destinations.RecipesScreen.ruta) {
            RecipesScreen(vm.recipesViewModel.llistaRecipes)
        }
    }

    Log.d("navigating ifs", alreadyInDb.value.toString())
    if (alreadyInDb.value){
        Log.d("navigating", "alreadyInDb")
        navController.navigate(Destinations.PantallaPrincipal.ruta){

        }
    }
    else if(showSignupConfig.value){
        Log.d("navigating", "showSignupConfig")
        navController.navigate(Destinations.SignupConfig.ruta){
            popUpTo(Destinations.Signup.ruta)
            {
                inclusive = false
            }
        }
    }

}



/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalBarraDeNavegacio(
    navController: NavHostController
) {
    Scaffold (
        topBar = {},
        bottomBar = {BarraDeNavegacio(navController)},
        content = {paddingValues -> Main(vm = MainViewModel(), navController = navController, )}
    )
}*/


@Composable
fun BarraDeNavegacio(navController: NavHostController) {
    val backStateEntry by navController.currentBackStackEntryAsState()
    NavigationBar() {
        ItemsBarraNavegacio.Items.forEach{
            NavigationBarItem(selected = it.ruta == backStateEntry?.destination?.route,
                onClick = {navController.navigate(it.ruta){
                    popUpTo(navController.graph.startDestinationId){
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }},
                icon = {Icon(it.imatge, "Navegacio")},
                label = { Text(it.titol)}
            )
        }
    }
}




