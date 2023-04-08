package com.example.foodieweekly_appv2.pantalles

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.firebase.RealtimeDatabase
import com.example.foodieweekly_appv2.model.enums.HealthLabels
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.*
import com.example.foodieweekly_appv2.viewmodel.LoginViewModel
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupConfig(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator){ //DONE!

    val username = remember { mutableStateOf("") }
    val firstName = remember {
        mutableStateOf("")
    }
    val lastName = remember {
        mutableStateOf("")
    }

    val showDialog = remember { mutableStateOf(false)}
    val messageDialog = remember { mutableStateOf("")}

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.fooddie_weekly_black),
            contentDescription = null,
            Modifier.size(150.dp))

        Box (Modifier.fillMaxWidth()){
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = "Hello there!",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )

                Text(text = "Let's start with some basic questions to configure your user!",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline
                )

                OutlinedTextFieldCustom(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), "Username (required)", "...", username)
                OutlinedTextFieldCustom(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), "First Name (required)", "", firstName)
                OutlinedTextFieldCustom(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), "Last Name (required)", "...", lastName)

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(30.dp), contentAlignment = Alignment.CenterEnd) {
                    Button(onClick =
                    {
                        val db = RealtimeDatabase()

                        if(!username.value.isNullOrEmpty()){
                            db.checkIfUsernameExists(username.value)

                            if(db.usernameExists.value){
                                showDialog.value = true
                                messageDialog.value = "Username already exists"
                            }
                            else if(firstName.value.isNullOrEmpty() && lastName.value.isNullOrEmpty()){
                                showDialog.value = true
                                messageDialog.value = "Names can't be empty"
                            }
                            else{
                                vm.user.firstName = firstName.value;
                                vm.user.lastName = lastName.value
                                vm.user.username = username.value


                                navController.navigate(Destinations.SignupUserBodyConfig.ruta)
                            }
                        }
                        else{
                            showDialog.value = true
                            messageDialog.value = "Username can't be empty"
                        }

                    },
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                            .shadow(0.dp),
                        contentPadding = PaddingValues(15.dp)){

                        Box(contentAlignment = Alignment.Center)
                        {
                            Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                                    modifier = Modifier
                                        .size(22.dp),
                                    contentDescription = "drawable icons",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "NEXT",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                        }

                        if(showDialog.value){

                            ShowAlert(showDialog = showDialog, "Atention", messageDialog.value, Icons.Filled.AccountCircle)
                        }


                    }
                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupUserBodyConfig(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator){ //DONE!


    val weight = remember {
        mutableStateOf("")
    }

    val height = remember {
        mutableStateOf("")
    }

    val isMale = remember { mutableStateOf(false) }

    val activeLevel = remember { mutableStateOf(-1) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDateText = remember { mutableStateOf("") }

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText.value = "$selectedDayOfMonth-${selectedMonth + 1}-$selectedYear"
        }, year, month, dayOfMonth
    )

    val errorText = remember {mutableStateOf("")}
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.fooddie_weekly_black),
            contentDescription = null,
            Modifier.size(150.dp))

        Box (Modifier.fillMaxWidth()){
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = "Now, let's talk about...",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )

                Text(text = "Answer the following questions to determine your caloric intake goal",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline
                )






                Text(text = "What is your gender?*",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp), contentAlignment = Alignment.Center) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(onClick =
                        {
                            isMale.value = true
                        }
                        ) {
                            Text("Male",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins)
                        }

                        OutlinedButton(onClick =
                        {
                            isMale.value = false
                        }
                        ) {
                            Text("Female",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins)
                        }
                    }
                }

                Text(text = "I am a " + if(isMale.value) "male" else "famale",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )



                Text(text = "When is your birthday?*",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp), contentAlignment = Alignment.Center) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(onClick =
                        {
                            datePicker.show()
                        }
                        ) {
                            Text("Select a Date",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins)
                        }

                    }

                }


                Text(text = "I was born in " + selectedDateText.value,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )








                Text(text = "Are you an active person?*",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp), contentAlignment = Alignment.Center) {



                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(onClick =
                        {
                            activeLevel.value = 0;
                        }
                        ) {
                            Text("Not very active",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins)
                        }

                        OutlinedButton(onClick =
                        {
                            activeLevel.value = 1;
                        }
                        ) {
                            Text(
                                "Kind of active",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins
                            )
                        }

                    }

                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp), contentAlignment = Alignment.Center) {



                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(onClick =
                        {
                            activeLevel.value = 2;
                        }
                        ) {
                            Text("Active",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins)
                        }

                        OutlinedButton(onClick =
                        {
                            activeLevel.value = 3;
                        }
                        ) {
                            Text(
                                "Very active",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = Poppins
                            )
                        }

                    }

                }


                Text(text = "I am" + when(activeLevel.value)
                {
                    0 -> " a not very active person"
                    1 -> " a little bit active"
                    2 -> " an active person"
                    3 -> " a very active person"
                    else -> {
                        "..."
                    }
                },
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )








                Text(text = "Tell us about your weight*",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                    /*TextField(value = "Hola", onValueChange = {},
                        label = { "Text(label)" },
                        placeholder = { Text("placeholder", textAlign = TextAlign.Center) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Black,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.wrapContentHeight().weight(1F),
                        singleLine = true)*/

                    OutlinedTextFieldCustomNumber(
                        Modifier
                            .fillMaxWidth()
                            .weight(1.5F), "Weight", "60", weight)

                    Text(text = "KG",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = Poppins, modifier = Modifier
                            .wrapContentWidth()
                            .weight(1F)
                            .padding(start = 10.dp)
                    )
                }







                Text(text = "And what about your height?*",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .padding(top = 40.dp)
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                    /*TextField(value = "Hola", onValueChange = {},
                        label = { "Text(label)" },
                        placeholder = { Text("placeholder", textAlign = TextAlign.Center) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Black,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.wrapContentHeight().weight(1F),
                        singleLine = true)*/

                    OutlinedTextFieldCustomNumber(
                        Modifier
                            .fillMaxWidth()
                            .weight(1.5F), "Height", "170", height)

                    Text(text = "CM",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = Poppins, modifier = Modifier
                            .wrapContentWidth()
                            .weight(1F)
                            .padding(start = 10.dp)
                    )
                }









                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(30.dp), contentAlignment = Alignment.CenterEnd) {
                    Button(onClick =
                    {
                        if(weight.value.isNullOrEmpty() && height.value.isNullOrEmpty()){
                            vm.showDialog.value = true
                            errorText.value = "You must define your height and weight!"
                        }
                        else if(weight.value.toInt() == 0 && height.value.toInt() == 0){
                            vm.showDialog.value = true
                            errorText.value = "Weight and height can't be 0!"
                        }
                        else if(activeLevel.value == -1){
                            vm.showDialog.value = true
                            errorText.value = "You must define your activity level!"
                        }
                        else if(selectedDateText.value.isNullOrEmpty()){
                            vm.showDialog.value = true
                            errorText.value = "You must enter your birthdate!"

                        }
                        else{

                            vm.userHealth.isMale = isMale.value
                            vm.userHealth.birthdate = selectedDateText.value
                            vm.userHealth.weight = weight.value
                            vm.userHealth.height = height.value
                            vm.userHealth.activeLevel = activeLevel.value

                            navController.navigate(Destinations.SignupUserDiet.ruta)
                        }
                    },
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .wrapContentHeight(Alignment.CenterVertically)
                            .shadow(0.dp),
                        contentPadding = PaddingValues(15.dp)){

                        Box(contentAlignment = Alignment.Center)
                        {
                            Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                                    modifier = Modifier
                                        .size(22.dp),
                                    contentDescription = "drawable icons",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "NEXT",
                                    color = MaterialTheme.colorScheme.onPrimary,
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
    }

    if(vm.showDialog.value){

        ShowAlert(showDialog = vm.showDialog, "Something went wrong", errorText.value, Icons.Filled.AccountCircle)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupUserDiet(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator) { //DONE!

    val dietIndex = remember { mutableStateOf(-1) }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.fooddie_weekly_black),
            contentDescription = null,
            Modifier.size(150.dp)
        )

        Box (Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Now, let's talk about...",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )

                Text(
                    text = "Answer the following questions about your diet to help you find the recipes that best suit you!",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline
                )

            }
        }



        Text(text = "What is your type of diet? *",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = Poppins,
            modifier = Modifier
                .padding(top = 40.dp)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 5.dp), contentAlignment = Alignment.Center) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(onClick =
                {
                    dietIndex.value = 0;
                },
                    border = BorderStroke(1.5.dp, color =
                    MaterialTheme.colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            "Classic Diet",
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = Poppins,
                            color = Color.Black
                        )

                        Image(painter = painterResource(id = R.drawable.granja),
                            contentDescription = "granja", modifier = Modifier.width(30.dp))
                    }
                }

                OutlinedButton(onClick =
                {
                    dietIndex.value = 1;
                },
                    border = BorderStroke(1.5.dp, color =
                    MaterialTheme.colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            "Pescatarian Diet",
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = Poppins,
                            color = Color.Black
                        )

                        Image(painter = painterResource(id = R.drawable.pez),
                            contentDescription = "pez", modifier = Modifier.width(30.dp))
                    }
                }

            }

        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 30.dp), contentAlignment = Alignment.Center) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(onClick =
                {
                    dietIndex.value = 2;
                },
                    border = BorderStroke(1.5.dp, color =
                    MaterialTheme.colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            "Vegetarian diet",
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = Poppins,
                            color = Color.Black
                        )

                        Image(painter = painterResource(id = R.drawable.comida_sana),
                            contentDescription = "comida_sana", modifier = Modifier.width(30.dp))
                    }
                }

                OutlinedButton(onClick =
                {
                    dietIndex.value = 3;
                },
                    border = BorderStroke(1.5.dp, color =
                    MaterialTheme.colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            "Vegan diet",
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = Poppins,
                            color = Color.Black
                        )

                        Image(painter = painterResource(id = R.drawable.sin_carne),
                            contentDescription = "sin_carne", modifier = Modifier.width(30.dp))
                    }

                }

            }

        }


        Text(text = "I follow" + when(dietIndex.value)
        {
            0 -> " a classic diet"
            1 -> " a pescatarian diet"
            2 -> " a vegetarian diet"
            3 -> " a vegan diet"
            else -> {
                "..."
            }
        },
            style = MaterialTheme.typography.titleSmall,
            fontFamily = Poppins,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )


        Box(
            Modifier
                .fillMaxWidth()
                .padding(30.dp), contentAlignment = Alignment.CenterEnd) {
            Button(onClick =
            {
                if(dietIndex.value == -1){
                    vm.showDialog.value = true
                    errorMessage.value = "You must select a diet type"
                }
                else{
                    when(dietIndex.value) {
                        1 -> vm.userHealth.userPreferences.add(HealthLabels.pescatarian)
                        2 -> vm.userHealth.userPreferences.add(HealthLabels.vegetarian)
                        3 -> vm.userHealth.userPreferences.add(HealthLabels.vegan)
                    }

                    navController.navigate(Destinations.SignupUserPreferences.ruta)
                }
            },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .shadow(0.dp),
                contentPadding = PaddingValues(15.dp)){

                Box(contentAlignment = Alignment.Center)
                {
                    Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                            modifier = Modifier
                                .size(22.dp),
                            contentDescription = "drawable icons",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "NEXT",
                            color = MaterialTheme.colorScheme.onPrimary,
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

    if(vm.showDialog.value){

        ShowAlert(showDialog = vm.showDialog, "Something went wrong", errorMessage.value, Icons.Filled.AccountCircle)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupUserPreferences(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator) { //DONE!

    val arr = enumValues<HealthLabels>()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.fooddie_weekly_black),
            contentDescription = null,
            Modifier.size(150.dp)
        )

        Box (Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Now, let's talk about...",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )

                Text(
                    text = "What are the requirements to find a good recipe for you?",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline
                )

            }
        }



        Box(
            Modifier
                .fillMaxWidth()
                .padding(30.dp)){
            Column(horizontalAlignment = Alignment.Start) {
                CheckBoxList(arr, vm.userHealth.userPreferences)
            }
        }







        Box(
            Modifier
                .fillMaxWidth()
                .padding(30.dp), contentAlignment = Alignment.CenterEnd) {
            Button(onClick =
            {



                navController.navigate(Destinations.SignupLastScreen.ruta)

            },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .shadow(0.dp),
                contentPadding = PaddingValues(15.dp)){

                Box(contentAlignment = Alignment.Center)
                {
                    Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                            modifier = Modifier
                                .size(22.dp),
                            contentDescription = "drawable icons",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "NEXT",
                            color = MaterialTheme.colorScheme.onPrimary,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupLastScreen(vm: SignupViewModel, navController: NavHostController, authenticator: Authenticator, signup : SignupViewModel) {

    val calories = remember { mutableStateOf(vm.calculateCalories().toString()) }

    var isError = remember {mutableStateOf(false)}
    var msgError = remember { mutableStateOf("")}

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.fooddie_weekly_black),
            contentDescription = null,
            Modifier.size(150.dp)
        )

        Box (Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Now, let's finish with...",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )

                Text(
                    text = "Based on your responses, we can calculate what would be your perfect " +
                            "caloric intake!",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline
                )

            }
        }

        val cal = vm.calculateCalories().toString();

        Text(text = "Your ideal caloric intake would be of $cal",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = Poppins,
            modifier = Modifier
                .padding(top = 40.dp)
        )

        Text(text = "You are free to change the value ",
            style = MaterialTheme.typography.titleSmall,
            fontFamily = Poppins,
            modifier = Modifier
                .padding(top = 40.dp)
        )


        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 5.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
            /*TextField(value = "Hola", onValueChange = {},
                label = { "Text(label)" },
                placeholder = { Text("placeholder", textAlign = TextAlign.Center) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Black,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorSupportingTextColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.wrapContentHeight().weight(1F),
                singleLine = true)*/

            OutlinedTextFieldCustomNumber(
                Modifier
                    .fillMaxWidth()
                    .weight(1.5F), "Calories", calories.value.toString(), calories)

            Text(text = "KCALS",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Poppins, modifier = Modifier
                    .wrapContentWidth()
                    .weight(1F)
                    .padding(start = 10.dp)
            )
        }




        Box(
            Modifier
                .fillMaxWidth()
                .padding(30.dp), contentAlignment = Alignment.CenterEnd) {
            Button(onClick =
            {
                vm.userHealth.userPreferences.forEach {
                        res -> Log.d("seeResults", res.name)
                }

                //Convert user labels to string

                vm.userHealth.preferencesToList()

                if(calories.value.isNullOrEmpty() || calories.value.toInt() < 1300){
                    //Show error
                    isError.value = true
                    msgError.value = "Calories can't be empty or under 1300."
                }
                else{
                    vm.user.caloricGoal = calories.value.toInt()
                    vm.user.preferences = vm.userHealth.userPreferencesStr
                }


                //Calculate calories based on health

                Log.d("signup", vm.user.username)
                authenticator.signup(vm.email.value, vm.password.value, vm.user, navController, signup)



            },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .shadow(0.dp),
                contentPadding = PaddingValues(15.dp)){

                Box(contentAlignment = Alignment.Center)
                {
                    Row(modifier = Modifier.wrapContentHeight(),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_arrow_forward_24),
                            modifier = Modifier
                                .size(22.dp),
                            contentDescription = "drawable icons",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "NEXT",
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                }


            }
        }


        if(isError.value){

            ShowAlert(showDialog = isError, "Something went wrong", msgError.value, Icons.Filled.AccountCircle)
        }

    }

}