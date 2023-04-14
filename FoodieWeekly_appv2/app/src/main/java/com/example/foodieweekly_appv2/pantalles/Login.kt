package com.example.foodieweekly_appv2.pantalles
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import com.example.foodieweekly_appv2.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.alreadyInDb
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.pantalles.ui.theme.FoodieWeekly_appv2Theme
import com.example.foodieweekly_appv2.showSignupConfig
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldCustom
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldCustomPassword
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldEmail
import com.example.foodieweekly_appv2.utils.ShowAlert
import com.example.foodieweekly_appv2.viewmodel.LoginViewModel
import com.example.foodieweekly_appv2.vm
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(activity : Activity) {

    val authenticator = vm.authenticator
    val navController = vm.navController
    val vm = vm.loginViewModel

    Log.d("autenticacion google", "Login")
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {

        Image(painter = painterResource(id = R.drawable.fooddie_weekly_black), contentDescription = null,
            Modifier.size(150.dp).weight(2F))

        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp).weight(1F), horizontalArrangement = Arrangement.Center){
            Text("LOG ", fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge, modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))
            Text("IN", fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary, modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))


        }

        Text("with",
            fontFamily = Poppins, fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1F)
        )

        Box(modifier = Modifier.padding(20.dp).weight(5F), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally){

                OutlinedTextFieldEmail(Modifier.fillMaxWidth().weight(2F), "Email", "Enter here yor email", vm.email, vm.validEmail)


                OutlinedTextFieldCustomPassword(Modifier.fillMaxWidth().weight(2F), "Password", "Enter here your password", vm.password)


                Box(Modifier.fillMaxWidth().weight(1.5F), contentAlignment = Alignment.CenterEnd){
                    Button(
                        onClick = {

                            authenticator.login(vm.email.value, vm.password.value, navController, vm)

                        },
                        enabled = vm.email.value.isNotEmpty() && vm.validEmail.value && vm.validPassword.value,
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
                            .shadow(0.dp),
                        contentPadding = PaddingValues(15.dp)
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
                                    text = "LOG IN",
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

            ShowAlert(showDialog = vm.showDialog, "Log In Failed", "Incorrect credentials!", Icons.Filled.AccountCircle)
        }


        vm.validEmail.value = android.util.Patterns.EMAIL_ADDRESS.matcher(vm.email.value).matches()
        vm.validPassword.value = vm.password.value.toString().length > 5

        Text("or with",
            fontFamily = Poppins, fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1F)
        )


        val alreadyDB = remember { mutableStateOf(false) }
        val toSignupConfig = remember { mutableStateOf(false) }

        IconButton(onClick = {

                            Log.d("autenticacion google", "signInIntent")
                            authenticator.loginGoogle(activity)

                             }, Modifier.size(60.dp).weight(1F)) {
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "google logo")
        }

        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp).weight(1F), horizontalArrangement = Arrangement.Center){


            TextButton(onClick = { navController.navigate(Destinations.Signup.ruta) }, modifier = Modifier.padding(0.dp)) {
                Text("Don't have an account yet? ", fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))

                Text("Sign up", fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary, modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally))
            }


        }



    }
}


