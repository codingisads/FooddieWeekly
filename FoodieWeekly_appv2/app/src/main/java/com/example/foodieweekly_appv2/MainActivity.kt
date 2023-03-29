package com.example.foodieweekly_appv2

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.model.HealthLabels
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.navigation.ItemsBarraNavegacio
import com.example.foodieweekly_appv2.pantalles.*
import com.example.foodieweekly_appv2.ui.theme.FoodieWeekly_appv2Theme
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.*
import com.example.foodieweekly_appv2.viewmodel.LoginViewModel
import com.example.foodieweekly_appv2.viewmodel.MainViewModel
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import java.util.*
import java.util.stream.Collectors.toList


val showSignupConfig =  mutableStateOf(false)
val alreadyInDb =  mutableStateOf(false)

class MainActivity : ComponentActivity() {

    val authenticator : Authenticator = Authenticator();
    val activity = this@MainActivity
    val vm = MainViewModel()
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

                    //PantallaPrincipal(Authenticator())
                    Main(vm, navController, activity, authenticator)
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

            authenticator.finishLogin(task, alreadyInDb, showSignupConfig)

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
            Signup(vm.signupViewModel, navController, authenticator)
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
            SignupLastScreen(vm.signupViewModel, navController, authenticator, vm.loginViewModel)
        }
        composable(Destinations.PantallaPrincipal.ruta) {
            PantallaPrincipal(authenticator)
        }
    }

    if (alreadyInDb.value){
        navController.navigate(Destinations.PantallaPrincipal.ruta)
    }
    else if(showSignupConfig.value){
        navController.navigate(Destinations.SignupConfig.ruta)
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

