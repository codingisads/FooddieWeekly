package com.example.foodieweekly_appv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.model.enums.TypeOfSingup
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.navigation.ItemsBarraNavegacio
import com.example.foodieweekly_appv2.pantalles.*
import com.example.foodieweekly_appv2.ui.theme.FoodieWeekly_appv2Theme
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.TabScreenMals
import com.example.foodieweekly_appv2.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

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

                    //ShowRecipeInfo()
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
            RecipesScreen(vm.recipesViewModel.llistaRecipes, vm.recipesViewModel, navController)
        }
        composable(Destinations.ShowRecipeInfo.ruta) {
            ShowRecipeInfo(vm.recipesViewModel.getActualRecipe())
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


@Composable
fun ShowRecipeInfo(actualRecipe: Recipe) {


    val tags = listOf("Gluten free", "Vegan")
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())) {

        if(!actualRecipe.images.lARGE.url.isNullOrEmpty()){
            AsyncImage(
                model = actualRecipe.images.lARGE.url,
                contentDescription = "recipeImage",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )
        }
        else if(!actualRecipe.images.rEGULAR.url.isNullOrEmpty()){
            AsyncImage(
                model = actualRecipe.images.rEGULAR.url,
                contentDescription = "recipeImage",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )
        }
        else{
            AsyncImage(
                model = actualRecipe.images.sMALL.url,
                contentDescription = "recipeImage",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )
        }


        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp), horizontalArrangement = Arrangement.SpaceAround){
            Image(painter = painterResource(R.drawable.clock), contentDescription = "time")
            Text("10 minutes", fontFamily = Poppins)
            Image(painter = painterResource(R.drawable.cals), contentDescription = "cals")
            Text("250kcals", fontFamily = Poppins)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Text(actualRecipe.label,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Poppins,
                modifier = Modifier.weight(3F))
            Image(painter = painterResource(R.drawable.cals), contentDescription = "cals",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F))

        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp)
                .horizontalScroll(rememberScrollState())) {

            for (i in 0 until actualRecipe.healthLabels.size){
                Box(
                    Modifier
                        .padding(end = 10.dp)
                        .clip(RoundedCornerShape(35.dp))
                        .background(
                            if (isSystemInDarkTheme())
                                Color(0xFF7d9e57)
                            else
                                Color(0xFFc0eb8f)
                        ),
                    contentAlignment = Alignment.Center)
                {
                    Text(actualRecipe.healthLabels[i], textAlign = TextAlign.Center,
                        fontFamily = Poppins,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                            ,
                        fontSize = 12.sp)
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)) {
            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "User",
                modifier = Modifier.padding(end=10.dp))
            Text("usernamo",textAlign = TextAlign.Center,
                fontFamily = Poppins,fontSize = 16.sp, fontWeight = FontWeight.ExtraLight)
        }

        TabScreenMals()

    }
}
