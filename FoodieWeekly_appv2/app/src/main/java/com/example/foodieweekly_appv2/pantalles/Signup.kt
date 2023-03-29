package com.example.foodieweekly_appv2.pantalles

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldCustomPassword
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldEmail
import com.example.foodieweekly_appv2.utils.ShowAlert
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {


        Image(painter = painterResource(id = R.drawable.fooddie_weekly_black), contentDescription = null,
            Modifier
                .size(150.dp)
                .weight(2F))

        Row(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .weight(1F), horizontalArrangement = Arrangement.Center){
            Text("SIGN ", fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))
            Text("UP", fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))


        }

        Text("with",
            fontFamily = Poppins, fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1F)
        )

        //FTextFields
        Box(modifier = Modifier
            .padding(20.dp)
            .weight(5F), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally){


                OutlinedTextFieldEmail(
                    Modifier
                        .fillMaxWidth()
                        .weight(2F), "Email", "Enter here yor email", vm.email, vm.validEmail)

                //Spacer(Modifier.height(25.dp))

                OutlinedTextFieldCustomPassword(
                    Modifier
                        .fillMaxWidth()
                        .weight(2F), "Password", "Enter here your password", vm.password)

                //Spacer(Modifier.height(25.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1.5F), contentAlignment = Alignment.CenterEnd){
                    Button(
                        onClick = {

                            authenticator.checkIfEmailIsNotRegistered(vm.email.value, vm, navController)

                            Log.d("FIREBASE ON signup", vm.goToUserPreferences.value.toString())



                            //Jump to user configuration screen
                            /*authenticator.signup(vm.email.value, vm.password.value)

                            Log.d("AUTHENTICATION DONE", authenticator.registered.value)

                            if(authenticator.hasRegistered.value){
                                vm.showDialog.value = true;
                            }
                            else{
                                vm.showDialog.value = true;
                            }*/

                          },
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                            .shadow(0.dp),
                        contentPadding = PaddingValues(15.dp),
                        enabled = vm.email.value.isNotEmpty() && vm.validEmail.value && vm.validPassword.value
                    ) {

                        Box(contentAlignment = Alignment.Center)
                        {
                            Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                                    modifier = Modifier
                                        .size(22.dp),
                                    contentDescription = "drawable icons",
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "SIGN UP",
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                        }
                    }

                }


            }
        }


        if(vm.showDialog.value){

            ShowAlert(showDialog = vm.showDialog, "Sign Up Failed", authenticator.registered.value, Icons.Filled.Email)
        }


        vm.validEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(vm.email.value).matches()
        vm.validPassword.value = vm.password.value.toString().length > 5

        /*if(vm.email.value.toString().length > 0){
            vm.invalidEmail.value = true
        }*/

        Text("or with",
            fontFamily = Poppins, fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1F)
        )

        IconButton(onClick = { /*TODO*/ },
            Modifier
                .size(60.dp)
                .weight(1F)) {
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "google logo")
        }

        Row(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .weight(1F), horizontalArrangement = Arrangement.Center){


            TextButton(onClick = { navController.navigate(Destinations.Login.ruta) }, modifier = Modifier.padding(0.dp)) {
                Text("Already have an account? ", fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))

                Text("Log in", fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary, modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))
            }


        }



    }
}
