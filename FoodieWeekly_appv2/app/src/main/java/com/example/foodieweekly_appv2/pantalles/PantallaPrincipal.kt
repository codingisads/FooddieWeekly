package com.example.foodieweekly_appv2.pantalles

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.TabScreen


@Composable
fun PantallaPrincipal(){

    val navController = com.example.foodieweekly_appv2.vm.navController
    val authenticator = com.example.foodieweekly_appv2.vm.authenticator
    val vmRecipes = com.example.foodieweekly_appv2.vm.recipesViewModel
    val vm = com.example.foodieweekly_appv2.vm.pantallaPrincipalViewModel

    vm.settingUp()

    val db = RealtimeDatabase()


    var mealsFromDay = remember { vm.mealsFromDay }
    var calorieGoal = remember { vm.userCaloricGoal}
    val dayCalories = remember { vm.selectedDayCalories }
    val percentage = remember { vm.calorieDayPercentage}
    //vm.getMealsFromDay( vm.weekId.value, vm.selectedIndex.value)

        Log.d("PantallaPrincipal", vm.dies.size.toString())
        Column (
            Modifier
                .verticalScroll(rememberScrollState())
                ) {

            Box(Modifier.padding(10.dp)){
                Column(){
                    Button(onClick = {

                            vmRecipes.getUserSavedRecipesIds()

                            vmRecipes.addMode.value = false;
                            Log.d("getRecipes recipesList", vmRecipes.llistaRecipes.value.size.toString())
                            navController.navigate(Destinations.RecipesScreen.ruta)

                    }) {
                        Text("Recipes")
                    }

                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            modifier = Modifier
                                .size(40.dp),
                            contentDescription = "drawable icons"
                        )
                    }

                    Box {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(text = "Good morning, " + vm.username.value + "!",
                                style = MaterialTheme.typography.titleLarge,
                                fontFamily = Poppins
                            )


                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Text(text = vm.currentDate,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Light,
                                    color = MaterialTheme.colorScheme.outline)
                                Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 15.dp)) {
                                    Icon(
                                        Icons.Rounded.Share,
                                        modifier = Modifier
                                            .size(30.dp),
                                        contentDescription = "drawable icons"
                                    )
                                }




                            }


                            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)) {
                                for (i in 0 until vm.dies.size){
                                    Box(
                                        Modifier
                                            .clip(RoundedCornerShape(13.dp))
                                            .width(40.dp)
                                            .height(50.dp)
                                            .background(/*if(isSystemInDarkTheme()) Color(0xFF464646)
                                    else Color(0xFFEAEAEA)*/ if (vm.selectedIndex.value == i)
                                                MaterialTheme.colorScheme.primary
                                            else  {
                                                if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA)
                                            }
                                            )
                                            .clickable {

                                                vm.selectedIndex.value = i
                                                Log.d("Actualizando la pntalla", "aqui")
                                                vm.getMealsFromDay(vm.weekId.value, i)



                                            }, Alignment.Center){
                                        Column(Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                vm.dies[i],
                                                fontFamily = Poppins,
                                                fontWeight = FontWeight.Light,
                                                fontSize = 11.sp

                                            )
                                            Text(
                                                vm.diesNum[i],
                                                fontFamily = Poppins,
                                                fontWeight = FontWeight.Light,
                                                fontSize = 11.sp
                                            )
                                        }

                                    }
                                }
                            }

                        }

                    }

                    Box(
                        Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                        Column(Modifier.fillMaxWidth()) {
                            Text(text = "Your calories today",
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = Poppins
                            )

                            Box(
                                Modifier
                                    .padding(top = 30.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(), contentAlignment = Alignment.Center){

                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                    Text(text = dayCalories.value.toString() +"/"+
                                            calorieGoal.value + " kcals",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = Poppins,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    CircularProgressIndicator(progress = 0.5f, modifier = Modifier.size(50.dp), color = MaterialTheme.colorScheme.primary)
                                    Text(text = percentage.value.toString() + "% of your daily goal!",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = Poppins, fontWeight = FontWeight.Light,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )

                                }

                            }

                        }


                    }
                }

            }


            TabScreen(mealsFromDay)



    }


}




@Composable
fun CustomTabs() {
    var selectedIndex = remember { mutableStateOf(0) }

    val list = listOf("Active", "Completed")

    TabRow(selectedTabIndex = selectedIndex.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex.value == index
            Tab(
                modifier = if (!selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.LightGray
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.primary
                    ),
                selected = selected,
                onClick = { selectedIndex.value = index },
                text = { Text(text = text, color = Color(0xff6FAAEE)) }
            )
        }
    }
}

