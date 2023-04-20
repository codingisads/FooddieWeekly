package com.example.foodieweekly_appv2.pantalles

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.vm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen() {
    val llistaRecipes = remember {com.example.foodieweekly_appv2.vm.recipesViewModel.llistaRecipes}
    val vm = com.example.foodieweekly_appv2.vm.recipesViewModel
    Log.d("recipesList", llistaRecipes.value.size.toString())

    val vs = remember { mutableStateOf("")}

    Column(Modifier.fillMaxWidth()) {

        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)){
            OutlinedTextField(
                value = vs.value,
                onValueChange = {vs.value = it},
                label = { Text("Insert the ingredients here") },
                placeholder = { Text("Chicken...", textAlign = TextAlign.Center) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,

                    placeholderColor = MaterialTheme.colorScheme.outline,

                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,

                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,

                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                ),
                leadingIcon = { Icon(painterResource(R.drawable.filter_alt), "",
                    modifier = Modifier.wrapContentWidth())},
                trailingIcon = { Icon(Icons.Rounded.Search, "")},
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone  = {
                        Log.d("RecipesScreen", "search for")

                        try {
                            if(!vs.value.isNullOrEmpty()){
                                vm.getRecipesOf(vs.value)
                                Log.d("RecipesScreen", "with ingredient")
                            }
                            else{
                                vm.get()
                                Log.d("RecipesScreen", "without ingredient")
                            }
                        }
                        catch(e : Exception){

                            Log.d("RecipesScreen", e.message.toString())
                        }

                    }
                )
            )
        }


        TabScreenRecipes()
    }


}

@Composable
fun TabScreenRecipes() {

    val tabs = listOf("All", "Saved", "Mine")
    val vm = vm.recipesViewModel
    var tabIndex = remember { mutableStateOf(0) }

    val showAll= remember { mutableStateOf(false)}
    val showSaved = remember { mutableStateOf(false)}
    val showMine = remember { mutableStateOf(false)}

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 30.dp)) {
        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { if(tabIndex.value == index) Text(title, fontFamily = Poppins) else Text(title, fontFamily = Poppins, color = Color.Gray) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index })
            }
        }
        when (tabIndex.value) {
            0 -> {showAll.value = true
                showSaved.value = false
                showMine.value = false}

            1 -> {showAll.value = false
                showSaved.value = true
                showMine.value = false}

            2 -> {showAll.value = false
                showSaved.value = false
                showMine.value = true}
        }
    }

    if(showAll.value){

        Column() {
            ShowRecipes(vm.llistaRecipes)
        }

    }
    else if(showSaved.value){
        Column(
            Modifier
                .fillMaxHeight()
                .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
            ShowSavedRecipes(vm.llistaSavedRecipes as MutableState<MutableList<Any>>)
        }
    }
}

