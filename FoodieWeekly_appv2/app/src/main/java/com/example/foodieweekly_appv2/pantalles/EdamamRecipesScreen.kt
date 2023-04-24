package com.example.foodieweekly_appv2.pantalles

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.model.recipesApi.Hit
import com.example.foodieweekly_appv2.model.recipesApi.Ingredient
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.retallaText
import com.example.foodieweekly_appv2.vm
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.roundToInt

@Composable
fun ShowRecipes(llistaRecipes : MutableState<MutableList<Any>>) {

    val vm = vm.recipesViewModel

    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))
    )
    {
        Log.d("recipesList into", llistaRecipes.value.size.toString())
        items(llistaRecipes.value)
        {element ->

            val hit = element as Hit
            RecipeElement(element.recipe)


        }

        if(listState.firstVisibleItemIndex == llistaRecipes.value.size - 8){
            Log.d("recipesList", "getMore")
            vm.getNextPage()
        }
    }
}

@Composable
fun ShowSavedRecipes(llistaRecipes : MutableState<MutableList<Any>>) {

    val vm = vm.recipesViewModel

    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))
    )
    {
        Log.d("recipesList into", llistaRecipes.value.size.toString())
        items(llistaRecipes.value)
        {element ->

            Log.d("ShowRecipes", "this is not a Hit!")
            RecipeElement(element as RecipeCustom, false)


        }

    }
}

@Composable
fun RecipeElement(recipeTo: Any, edamamRecipe : Boolean = true) {

    val navController = vm.navController

    if(edamamRecipe){
        if(vm.recipesViewModel.addMode.value){

            val recipe = recipeTo as Recipe
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(15.dp))
                ,
                horizontalAlignment = Alignment.Start
            ) {

                Box(modifier = Modifier
                    .heightIn(min = 260.dp)
                    .background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.TopCenter){
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()){


                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                            if(recipe.images.lARGE != null){

                                Log.d("imageURL", recipe.images.lARGE.toString())
                                AsyncImage(
                                    model = recipe.images.lARGE.url,
                                    contentDescription = "recipeImage",
                                    modifier = Modifier
                                        .clickable {

                                        }
                                        .fillMaxSize()
                                        .clip(
                                            RoundedCornerShape(15.dp)
                                        )
                                )
                            }
                            else if(recipe.images.rEGULAR != null){

                                Log.d("imageURL", recipe.images.rEGULAR.toString())
                                AsyncImage(
                                    model = recipe.images.rEGULAR.url,
                                    contentDescription = "recipeImage",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .clickable {

                                        }
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(15.dp))
                                )
                            }
                            else{
                                Log.d("imageURL", recipe.images.sMALL.toString())
                                AsyncImage(
                                    model = recipe.images.sMALL.url,
                                    contentDescription = "recipeImage",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .clickable {

                                        }
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(15.dp))

                                )
                            }

                            Image(painter = painterResource(id = com.example.foodieweekly_appv2.R.drawable.add_circle_outline_recipes),
                                contentDescription = "addRecipeButton",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {

                                    })

                        }




                        Text(text = retallaText(recipe.label, 25),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, top = 10.dp),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )

                        Text(text = (recipe.calories.toInt() / recipe.yield).toInt().toString() + "cals/serving",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            fontFamily = Poppins,
                            lineHeight = 16.sp
                        )
                        Text(text = recipe.totalTime.toInt().toString() + " minutes",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            fontFamily = Poppins,
                            lineHeight = 16.sp
                        )
                    }
                }

            }
        }
        else{

            val recipe = recipeTo as Recipe

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        vm.recipesViewModel.setActualRecipe(recipe)
                        navController.navigate(Destinations.ShowRecipeInfo.ruta) {
                            popUpTo(Destinations.ShowRecipeInfo.ruta)
                            launchSingleTop = true
                        }
                    },
                horizontalAlignment = Alignment.Start
            ) {

                Box(modifier = Modifier
                    .heightIn(min = 260.dp)
                    .background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.TopCenter){
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()){



                        if(recipe.images.lARGE != null){

                            Log.d("imageURL", recipe.images.lARGE.toString())
                            AsyncImage(
                                model = recipe.images.lARGE.url,
                                contentDescription = "recipeImage",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp))
                            )
                        }
                        else if(recipe.images.rEGULAR != null){

                            Log.d("imageURL", recipe.images.rEGULAR.toString())
                            AsyncImage(
                                model = recipe.images.rEGULAR.url,
                                contentDescription = "recipeImage",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp))
                            )
                        }
                        else{
                            Log.d("imageURL", recipe.images.sMALL.toString())
                            AsyncImage(
                                model = recipe.images.sMALL.url,
                                contentDescription = "recipeImage",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp))

                            )
                        }


                        Text(text = retallaText(recipe.label, 25),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, top = 10.dp),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )

                        Text(text = (recipe.calories.toInt() / recipe.yield).toInt().toString() + "cals/serving",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            fontFamily = Poppins,
                            lineHeight = 16.sp
                        )
                        Text(text = recipe.totalTime.toInt().toString() + " minutes",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            fontFamily = Poppins,
                            lineHeight = 16.sp
                        )
                    }
                }

            }
        }
    }
    else{

        val recipe = recipeTo as RecipeCustom

        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    vm.recipesViewModel.setActualRecipe(recipe)
                    navController.navigate(Destinations.ShowRecipeInfo.ruta) {
                        popUpTo(Destinations.ShowRecipeInfo.ruta)
                        launchSingleTop = true
                    }
                },
            horizontalAlignment = Alignment.Start
        ) {

            Box(modifier = Modifier
                .heightIn(min = 260.dp)
                .background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.TopCenter){
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()){


                    Log.d("RecipeElement", recipe.imageUrl)

                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = "recipeImage",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp))
                    )

                    Text(text = retallaText(recipe.label, 25),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    )

                    Text(text = recipe.kcalsPerServing.toString() + "cals/serving",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        fontFamily = Poppins,
                        lineHeight = 16.sp
                    )
                    Text(text = recipe.time.toString() + " minutes",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        fontFamily = Poppins,
                        lineHeight = 16.sp
                    )
                }
            }

        }
    }




}

@Composable
fun ShowRecipeInfo(recipe: Any) {

    var originalText = remember { mutableStateOf("")}

    val newText = remember { mutableStateOf("")}

    val colorsLight = listOf<Color>(Color(0xFFf0cb67), Color(0xFFc0eb8f),
        Color(0xFFf09767), Color(0xFF78D6B8), Color(0xFF8B81E6)
    )

    val colorsDark = listOf<Color>(Color(0xFFCFAC4C), Color(0xFF89C75D),
        Color(0xFFD1724D), Color(0xFF4A99A0), Color(0xFF4959AA)
    )

    if(recipe is Recipe){

        val actualRecipe = recipe
        val savedRecipe = remember { mutableStateOf(
            vm.recipesViewModel.recipesXAnnotations.contains(actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))) }


        vm.recipesViewModel.getRecipesSaves(actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {

            Log.d("ShowRecipeInfo", "checking images")
            if(actualRecipe.images.lARGE != null){

                Log.d("ShowRecipeInfo", "big images")
                AsyncImage(
                    model = actualRecipe.images.lARGE.url,
                    contentDescription = "recipeImage",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                )
            }
            else if(actualRecipe.images.rEGULAR != null){

                Log.d("ShowRecipeInfo", "medium images")
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

                Log.d("ShowRecipeInfo", "smol images")
                AsyncImage(
                    model = actualRecipe.images.sMALL.url,
                    contentDescription = "recipeImage",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                )
            }



            Log.d("ShowRecipeInfo", "writing info")

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){

                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.saves), contentDescription = "saves")
                Text(vm.recipesViewModel.selectedRecipeSaves.value.toString()+" saves", fontFamily = Poppins, fontSize = 12.sp)

                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.clock), contentDescription = "time")
                Text(actualRecipe.totalTime.toInt().toString()+" minutes", fontFamily = Poppins, fontSize = 12.sp)


                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.cals), contentDescription = "cals")
                Text((actualRecipe.calories.toInt()/actualRecipe.yield.toInt()).toString()+"kcals", fontFamily = Poppins, fontSize = 12.sp)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(actualRecipe.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Poppins,
                    modifier = Modifier.weight(3F))


                if(savedRecipe.value){

                    Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.bookmark), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {
                                    savedRecipe.value = !savedRecipe.value

                                    vm.recipesViewModel.removeRecipeFromSavedRecipes(actualRecipe)

                                } catch (e: Exception) {
                                    Log.d("savedRecipes", e.message.toString())
                                }


                            }
                            .fillMaxHeight()
                            .weight(1F))

                }
                else{
                    Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.bookmark_border), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {

                                    //Guardar recipe


                                    savedRecipe.value = !savedRecipe.value

                                    vm.recipesViewModel.addRecipeToSavedRecipes(actualRecipe, originalText.value)
                                } catch (e: Exception) {
                                    Log.d("savedRecipes error", e.message.toString())
                                }


                            }
                            .fillMaxHeight()
                            .weight(1F))
                }



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
                                    colorsDark[i % 5]
                                else
                                    colorsLight[i % 5]
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
                Text("Edamam",textAlign = TextAlign.Center,
                    fontFamily = Poppins,fontSize = 16.sp, fontWeight = FontWeight.ExtraLight)
            }

            TabScreenMeals(actualRecipe)


            RecipeAnotations(originalText, newText, savedRecipe.value,
                actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))

        }
    }
    else{
        val actualRecipe = recipe as RecipeCustom

        val savedRecipe = remember { mutableStateOf(
            vm.recipesViewModel.recipesXAnnotations.contains(
                actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))) }


        originalText.value = vm.recipesViewModel.recipesXAnnotations[actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", "")]
            .toString()

        if(originalText.value == "null"){
            originalText.value = ""
        }


        newText.value = originalText.value


        vm.recipesViewModel.getRecipesSaves(actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {

            Log.d("ShowRecipeInfo", "checking images")

            AsyncImage(
                model = actualRecipe.imageUrl,
                contentDescription = "recipeImage",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            )

            Log.d("ShowRecipeInfo img url", actualRecipe.imageUrl)





            //var storage = FirebaseStorage.getInstance().reference.child("si").putStream()

            Log.d("ShowRecipeInfo", "writing info")

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){

                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.saves), contentDescription = "saves")
                Text(vm.recipesViewModel.selectedRecipeSaves.value.toString()+" saves", fontFamily = Poppins, fontSize = 12.sp)

                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.clock), contentDescription = "time")
                Text(actualRecipe.time.toInt().toString()+" minutes", fontFamily = Poppins, fontSize = 12.sp)


                Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.cals), contentDescription = "cals")
                Text(actualRecipe.kcalsPerServing.toString()+"kcals", fontFamily = Poppins, fontSize = 12.sp)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Text(actualRecipe.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Poppins,
                    modifier = Modifier.weight(3F))


                if(savedRecipe.value){

                    Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.bookmark), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {
                                    savedRecipe.value = !savedRecipe.value

                                    vm.recipesViewModel.removeRecipeFromSavedRecipes(actualRecipe)

                                } catch (e: Exception) {
                                    Log.d("savedRecipes", e.message.toString())
                                }


                            }
                            .fillMaxHeight()
                            .weight(1F))

                }
                else{
                    Image(painter = painterResource(com.example.foodieweekly_appv2.R.drawable.bookmark_border), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {

                                    //Guardar recipe


                                    savedRecipe.value = !savedRecipe.value

                                    vm.recipesViewModel.addRecipeToSavedRecipes(actualRecipe, originalText.value)
                                } catch (e: Exception) {
                                    Log.d("savedRecipes error", e.message.toString())
                                }


                            }
                            .fillMaxHeight()
                            .weight(1F))
                }



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
                                    colorsDark[i % 5]
                                else
                                    colorsLight[i % 5]
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
                Text("Edamam",textAlign = TextAlign.Center,
                    fontFamily = Poppins,fontSize = 16.sp, fontWeight = FontWeight.ExtraLight)
            }

            TabScreenMeals(actualRecipe)

            RecipeAnotations(originalText, newText, savedRecipe.value,
                actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", ""))

        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAnotations(originalText : MutableState<String>, newText : MutableState<String>, savedRecipe : Boolean,
recipeUri : String){




    Box(Modifier
        .padding(top = 100.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
        .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {

        Column(Modifier.fillMaxWidth()) {

            Text("Your annotations", fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Poppins
            , modifier = Modifier.fillMaxWidth().padding(start = 25.dp, top = 25.dp))

            Box(Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .padding(25.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)) {

                BasicTextField(value = newText.value, onValueChange = {newText.value = it},
                    Modifier.fillMaxSize().padding(10.dp),
                    textStyle = TextStyle(
                        fontFamily = Poppins, color = MaterialTheme.colorScheme.onSurface))
            }

            Box(Modifier
                .fillMaxWidth()
                .padding(25.dp), contentAlignment = Alignment.CenterEnd) {
                Button(onClick = {
                    originalText.value = newText.value

                    if(savedRecipe){
                        FirebaseDatabase.getInstance().reference.root
                            .child("Users")
                            .child(vm.authenticator.currentUID.value)
                            .child("savedRecipes")
                            .child(recipeUri).setValue(originalText.value)
                    }
                    },

                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary),

                    enabled = originalText.value != newText.value

                ) {
                    Text(text = "SAVE", fontFamily = Poppins, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}

@Composable
fun TabScreenMeals(recipeRaw : Any) {
    var tabIndex = remember { mutableStateOf(0) }

    val showIngredients = remember { mutableStateOf(false) }
    val showNutrition = remember { mutableStateOf(false) }
    val showSteps = remember { mutableStateOf(false) }
    val tabs = listOf("Ingredients", "Steps", "Nutrition")

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
            0 -> {
                showIngredients.value = true
                showNutrition.value = false
                showSteps.value =false
            }
            1 -> {
                showIngredients.value = false
                showNutrition.value = false
                showSteps.value = true
            }

            2 -> {
                showIngredients.value = false
                showNutrition.value = true
                showSteps.value = false
            }
        }
    }


    if(recipeRaw is Recipe){
        val recipe = recipeRaw

        if(showIngredients.value){
            Column() {
                ShowRecipeIngredients(recipe.ingredients, recipe.yield.toInt())
            }

        }
        else if(showNutrition.value){

            ShowRecipeNutrition(recipe)

        }
        else if(showSteps.value){

            Column(
                Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
                ShowRecipeSteps()
            }

        }
    }
    else{
        val recipe = recipeRaw as RecipeCustom

        if(showIngredients.value){
            Column() {
                ShowRecipeCustomIngredients(recipe.ingredientsNameList, recipe.ingredientsQuantityList, recipe.ingredientsMeasureList, recipe.servings)
            }

        }
        else if(showNutrition.value){

            ShowRecipeNutrition(recipe)

        }
        else if(showSteps.value){

            Column(
                Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
                ShowRecipeSteps(recipe.username == "Edamam")
            }

        }
    }


}

@Composable
fun ShowRecipeIngredients(ingredientsList : List<Any>, servings : Int){
    Column(Modifier
        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
        .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA)))
    {
        Text("You will need", Modifier
            .padding(start = 40.dp, top=20.dp),
            fontFamily = Poppins,fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ){

                Column(Modifier
                    .fillMaxWidth()) {

                    Text("Per " + servings +  " servings",
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp), fontSize = 10.sp,
                        fontFamily = Poppins, fontWeight = FontWeight.ExtraLight, textAlign = TextAlign.End
                    )

                    for (i in 0 until ingredientsList.size){

                        if(ingredientsList[i] is Ingredient){

                            val ingredients = ingredientsList[i] as Ingredient
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp, end = 20.dp,
                                        bottom = 20.dp
                                    )) {
                                Text(ingredients.food, Modifier.weight(2F), fontFamily = Poppins, fontWeight = FontWeight.Bold)
                                if(ingredients.quantity.roundToInt() > 0){
                                    Text(ingredients.quantity.roundToInt().toString()+" "+ingredients.measure,
                                        Modifier.weight(1F),
                                        textAlign = TextAlign.End, fontFamily = Poppins, fontSize = 12.sp)
                                }
                                else{
                                    Text("as pleased",
                                        Modifier.weight(1F), textAlign = TextAlign.End, fontFamily = Poppins, fontSize = 12.sp)
                                }
                            }
                        }


                    }
                }
            }
        }


    }
}

@Composable
fun ShowRecipeCustomIngredients(ingrName : List<String>, ingrQuantity : List<Int>, ingrMeasure : List<String>, servings : Int){
    Column(Modifier
        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
        .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA)))
    {
        Text("You will need", Modifier
            .padding(start = 40.dp, top=20.dp),
            fontFamily = Poppins,fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ){

                Column(Modifier
                    .fillMaxWidth()) {

                    Text("Per " + servings +  " servings",
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp), fontSize = 10.sp,
                        fontFamily = Poppins, fontWeight = FontWeight.ExtraLight, textAlign = TextAlign.End
                    )

                    for (i in 0 until ingrName.size){

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp, end = 20.dp,
                                    bottom = 20.dp
                                )) {
                            Text(ingrName[i], Modifier.weight(2F), fontFamily = Poppins, fontWeight = FontWeight.Bold)
                            Text(ingrQuantity[i].toString()+" "+ingrMeasure[i],
                                Modifier.weight(1F),
                                textAlign = TextAlign.End, fontFamily = Poppins, fontSize = 12.sp)

                        }
                        }
                    }
                }
            }
        }


    }

@Composable
fun ShowRecipeSteps(isEdamam : Boolean = true){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(30.dp)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center){

        Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 40.dp)){
            Text("There are no steps available for this recipe :-(", fontFamily = Poppins, textAlign = TextAlign.Center)
        }

    }
}

@Composable
fun ShowRecipeNutrition(recipeRaw : Any) {

    val colorsLight = listOf(Color(0xFF6AA73F), Color(0xFFD87F22), Color(0xFFE03535))
    val colorsDark = listOf(Color(0xFF33971D), Color(0xFFE76016), Color(0xFFB81F3E))

    if(recipeRaw is Recipe){
        val recipe = recipeRaw as Recipe

        Column(
            Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center){

                Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 40.dp), contentAlignment = Alignment.Center){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(recipe.yield.toInt().toString()+" servings", fontFamily = Poppins, textAlign = TextAlign.Center)
                        Text((recipe.calories.toInt()/recipe.yield.toInt()).toString()+" kcal/serving", fontFamily = Poppins,
                            textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )




                        Divider(Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline)))

                        for (i in 0 until recipe.digest.size){

                            if(recipe.digest[i].label == "Protein" ||
                                recipe.digest[i].label == "Carbs" ||
                                recipe.digest[i].label == "Fat"){
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                    Box(
                                        Modifier
                                            .width(15.dp)
                                            .height(15.dp)
                                            .clip(RoundedCornerShape(55.dp))
                                            .background(
                                                if (isSystemInDarkTheme()) {
                                                    colorsDark[i % 3]
                                                } else {
                                                    colorsLight[i % 3]
                                                }
                                            )) {

                                    }

                                    Text(recipe.digest[i].label.uppercase(), Modifier.padding(start = 5.dp), fontFamily = Poppins )

                                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                        Text(recipe.digest[i].total.toInt().toString()+recipe.digest[i].unit,
                                            fontFamily = Poppins, textAlign = TextAlign.End, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if(i == 2)
                                    Divider(Modifier
                                        .padding(top = 15.dp, bottom = 15.dp)
                                        .border(
                                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)))
                            }
                            else{

                                Row(

                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){


                                    Text(recipe.digest[i].label, Modifier.padding(start = 5.dp), fontFamily = Poppins , fontSize = 10.sp)

                                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                        Text(recipe.digest[i].total.toInt().toString()+recipe.digest[i].unit,
                                            fontFamily = Poppins, textAlign = TextAlign.End, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                        }
                    }

                }

            }
        }
    }
    else{
        val recipe = recipeRaw as RecipeCustom
        Column(
            Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center){

                Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 40.dp), contentAlignment = Alignment.Center){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(recipe.servings.toString()+" servings", fontFamily = Poppins, textAlign = TextAlign.Center)
                        Text((recipe.kcalsPerServing/recipe.servings).toString()+" kcal/serving", fontFamily = Poppins,
                            textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )




                        Divider(Modifier.border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline)))

                        for (i in 0 until recipe.nutritionLabels.size){

                            if(recipe.nutritionLabels[i] == "Protein" ||
                                recipe.nutritionLabels[i] == "Carbs" ||
                                recipe.nutritionLabels[i] == "Fat"){
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                                    Box(
                                        Modifier
                                            .width(15.dp)
                                            .height(15.dp)
                                            .clip(RoundedCornerShape(55.dp))
                                            .background(
                                                if (isSystemInDarkTheme()) {
                                                    colorsDark[i % 3]
                                                } else {
                                                    colorsLight[i % 3]
                                                }
                                            )) {

                                    }

                                    Text(recipe.nutritionLabels[i].uppercase(), Modifier.padding(start = 5.dp), fontFamily = Poppins )

                                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                        Text(recipe.nutritionQuantity[i].toString()+recipe.nutritionUnits[i],
                                            fontFamily = Poppins, textAlign = TextAlign.End, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if(i == 2)
                                    Divider(Modifier
                                        .padding(top = 15.dp, bottom = 15.dp)
                                        .border(
                                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)))
                            }
                            else{

                                Row(

                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){


                                    Text(recipe.nutritionLabels[i], Modifier.padding(start = 5.dp), fontFamily = Poppins , fontSize = 10.sp)

                                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                        Text(recipe.nutritionQuantity[i].toString()+recipe.nutritionUnits[i],
                                            fontFamily = Poppins, textAlign = TextAlign.End, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                        }
                    }

                }

            }
        }
    }



}