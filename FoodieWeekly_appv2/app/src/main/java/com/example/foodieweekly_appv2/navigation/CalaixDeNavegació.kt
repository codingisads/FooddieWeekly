package com.example.foodieweekly_appv2.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.ShowAlertAddCalendar
import com.example.foodieweekly_appv2.vm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalCalaixDeNavegacio(navController: NavHostController) {
    val estatDrawer = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var rutaActual =  remember{ mutableStateOf(Destinations.PantallaPrincipal.ruta) }
    var list = remember {vm.pantallaPrincipalViewModel.calendarList}

    val showDialog = remember { mutableStateOf(false)}

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

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text("Your calendars",
                        Modifier.padding(30.dp),
                    fontFamily = Poppins,
                    fontSize = 22.sp, textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold)


                    OutlinedButton(onClick = {

                        showDialog.value = true

                    }) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween){
                            Text("Add calendar ", fontFamily = Poppins,
                                fontSize = 18.sp, textAlign = TextAlign.Center,
                                softWrap = true)
                            Image(painter = painterResource(R.drawable.add), contentDescription = "add_calendar")
                        }

                    }

                    if(showDialog.value){

                        ShowAlertAddCalendar(showDialog)
                    }

                    Log.d("PrincipalCalaixDeNavegacio", list.value.calendarList.value.size.toString())
                    for (i in 0 until list.value.calendarList.value.size){

                        Button(onClick = {
                            vm.pantallaPrincipalViewModel.selectedIndexCalendar.value = i
                            vm.pantallaPrincipalViewModel.changingCalendar()
                        },
                            Modifier.padding(top = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                                containerColor = if(vm.pantallaPrincipalViewModel.selectedIndexCalendar.value == i)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.primaryContainer
                                )) {

                            if(list.value.calendarList.value[i].ownerUID == vm.authenticator.currentUID.value){
                                Text(list.value.calendarList.value[i].calendarName +" (Yours)",
                                    fontFamily = Poppins,
                                    fontSize = 18.sp, textAlign = TextAlign.Center,
                                    softWrap = true)
                            }
                            else{
                                Text(list.value.calendarList.value[i].calendarName + "("+list.value.calendarList.value[i].ownerUsername+ ")",
                                    fontFamily = Poppins,
                                    fontSize = 18.sp, textAlign = TextAlign.Center,
                                    softWrap = true)
                            }

                        }


                    }
                }


            }
        },
        content = {
            PrincipalBarraDeNavegacio(vm = vm, navController = navController, estatDrawer, scope)
        })

}