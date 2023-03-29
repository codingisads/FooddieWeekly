package com.example.foodieweekly_appv2.utils

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import com.example.foodieweekly_appv2.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.foodieweekly_appv2.model.HealthLabels
import com.example.foodieweekly_appv2.ui.theme.Poppins
import java.io.FileReader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldCustomPassword(modifier : Modifier, label : String, placeholder : String, password : MutableState<String>){

    var passwordVisible = remember {mutableStateOf(false)}
    androidx.compose.material3.OutlinedTextField(
        value = password.value,
        onValueChange = {password.value = it},
        label = { Text(label) },
        placeholder = { Text(placeholder, textAlign = TextAlign.Center) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Black
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier.wrapContentHeight(),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible.value) {
                ImageVector.vectorResource(R.drawable.visibility_black_24dp) // Please provide localized description for accessibility services
            } else ImageVector.vectorResource(R.drawable.visibility_off_black_24dp)

            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = {passwordVisible.value = !passwordVisible.value}){
                Icon(imageVector  = image, description)
            }
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldEmail(modifier : Modifier, label : String, placeholder : String, inputTxt : MutableState<String>, validEmail : MutableState<Boolean>){


    androidx.compose.material3.OutlinedTextField(
        value = inputTxt.value,
        onValueChange = {inputTxt.value = it},
        label = { Text(label) },
        placeholder = { Text(placeholder, textAlign = TextAlign.Center) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Black,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        ),
        isError = !validEmail.value,
        supportingText = {
            if(!validEmail.value){
                Text(modifier = Modifier.fillMaxWidth(),
                    text = "Incorrect email format",
                    color = MaterialTheme.colorScheme.error)
            }

            Log.d("Email", validEmail.value.toString())

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier.wrapContentHeight(),
        singleLine = true
    )

    //!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldCustom(modifier : Modifier, label : String, placeholder : String, inputTxt : MutableState<String>){

    androidx.compose.material3.OutlinedTextField(
        value = inputTxt.value,
        onValueChange = {inputTxt.value = it},
        label = { Text(label) },
        placeholder = { Text(placeholder, textAlign = TextAlign.Center) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Black,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier.wrapContentHeight(),
        singleLine = true
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldCustomNumber(modifier : Modifier, label : String, placeholder : String, inputTxt : MutableState<String>){

    androidx.compose.material3.OutlinedTextField(
        value = inputTxt.value,
        onValueChange = {inputTxt.value = it},
        label = { Text(label) },
        placeholder = { Text(placeholder, textAlign = TextAlign.Center) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Black,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier.wrapContentHeight(),
        singleLine = true
    )
}

@Composable
fun ShowAlert(showDialog: MutableState<Boolean>, title: String, text: String, icon : ImageVector){
    Log.d("ALERT", text)
    //if(showDialog.value){
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                Button(onClick = {showDialog.value = false }) {
                    Text("Confirm", fontFamily = Poppins)
                }
            },
            title = {
                Text(title, fontFamily = Poppins, softWrap = true)
            },
            text = {
                Text(text, fontFamily = Poppins, softWrap = true, overflow = TextOverflow.Visible)
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            icon = {
                Icon(
                    imageVector = icon,
                    modifier = Modifier
                        .size(22.dp),
                    contentDescription = "drawable icons",
                    tint = Color.Unspecified
                )
            }

        )
    //}
}

@Composable
fun TabScreen() {
    var tabIndex = remember { mutableStateOf(0) }

    val showMeals = remember { mutableStateOf(false)}
    val showNutrition = remember { mutableStateOf(false)}
    val tabs = listOf("Meals", "Your balance")

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 30.dp)) {
        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { if(tabIndex.value == index) Text(title, fontFamily = Poppins) else Text(title, fontFamily = Poppins, color = Color.Gray) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index }
                )
            }
        }
        when (tabIndex.value) {
            0 -> {showMeals.value = true
                showNutrition.value = false}
            1 -> {showMeals.value = false
                showNutrition.value = true}
        }
    }

    if(showMeals.value){
        Column() {
            Meals()
        }

    }
}

@Composable
fun Meals() {
    Column() {

        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(25.dp))
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(25.dp)
                )) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)) {
                    Text("Breakfast", Modifier.padding(end=15.dp),
                        fontFamily = Poppins, style = MaterialTheme.typography.labelMedium)
                    Text("368kcal",
                        fontFamily = Poppins, style = MaterialTheme.typography.labelMedium)
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    /*Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "",
                        Modifier
                            .weight(1F)
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(15.dp, 0.dp, 0.dp, 15.dp)
                            ))*/

                    AsyncImage(
                        model = "https://i.pinimg.com/736x/47/cd/a8/47cda8eca5f5f013d14ce7dd6b408bfd.jpg",
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(15.dp, 0.dp, 0.dp, 15.dp)
                            )
                    )


                    Box(
                        Modifier
                            .weight(2F)
                            .fillMaxHeight(), contentAlignment = Alignment.Center){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(
                                Modifier
                                    .weight(5F)
                                    .padding(start = 10.dp)) {
                                Text("Oat chia pudding with walnuts and pistachio",
                                    fontFamily = Poppins, style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text("Delicious meal to start the day!",
                                    fontFamily = Poppins, style = MaterialTheme.typography.bodySmall)
                            }


                            Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_forward_24),
                                contentDescription = "",
                                Modifier
                                    .fillMaxSize()
                                    .weight(1F))
                        }

                    }
                }
            }


        }
    }
}

@Composable
fun CheckBoxList(labels : Array<HealthLabels>, choosed : MutableList<HealthLabels>) {


    Column() {
        labels.forEachIndexed {
                index ,label  ->
            var check = remember { mutableStateOf(false) }


            if( label != HealthLabels.pescatarian
                && label != HealthLabels.vegetarian
                && label != HealthLabels.vegan ){
                LabelledCheckbox(
                    checked = check.value,
                    onCheckedChange =
                    {
                        check.value = it
                        if(check.value){
                            choosed.add(label)
                        }
                        else{
                            choosed.remove(label)
                        }

                    },
                    label = label.name.replace('_', ' '),
                    enabled = true
                )
            }

        }
    }

}



@Composable
fun LabelledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors
        )
        Spacer(Modifier.width(32.dp))
        Text(label, style = MaterialTheme.typography.titleSmall,
            fontFamily = Poppins,
            fontWeight = FontWeight.Light)
    }
}