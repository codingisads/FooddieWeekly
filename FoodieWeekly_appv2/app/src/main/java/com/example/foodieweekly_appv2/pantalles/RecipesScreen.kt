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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.model.RecipeCustom
import com.example.foodieweekly_appv2.model.recipesApi.Hit
import com.example.foodieweekly_appv2.model.recipesApi.Ingredient
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.retallaText
import com.example.foodieweekly_appv2.viewmodel.RecipesViewModel
import com.example.foodieweekly_appv2.vm
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen() {
    val llistaRecipes = remember {com.example.foodieweekly_appv2.vm.recipesViewModel.llistaRecipes}
    val navController = com.example.foodieweekly_appv2.vm.navController
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


        TabScreenRecipes(listOf("All", "Saved", "Mine"), llistaRecipes, vm, navController)
    }


}

@Composable
fun ShowRecipes(
    llistaRecipes: MutableState<MutableList<Hit>>,
    vm: RecipesViewModel,
    navController: NavHostController
) {

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
            RecipeElement(element.recipe, navController)

        }

        if(listState.firstVisibleItemIndex == llistaRecipes.value.size - 8){
            Log.d("recipesList", "getMore")
            vm.getNextPage()
        }
    }
}


@Composable
fun RecipeElement(recipe: Recipe, navController: NavHostController) {

    if(vm.recipesViewModel.addMode.value){
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

                        Image(painter = painterResource(id = R.drawable.add_circle_outline_recipes),
                            contentDescription = "addRecipeButton", modifier = Modifier.fillMaxSize())

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


@Composable
fun ShowRecipeInfo(actualRecipe: Recipe) {

    val colorsLight = listOf<Color>(Color(0xFFf0cb67), Color(0xFFc0eb8f),
        Color(0xFFf09767), Color(0xFF78D6B8), Color(0xFF8B81E6)
    )

    val colorsDark = listOf<Color>(Color(0xFFCFAC4C), Color(0xFF89C75D),
        Color(0xFFD1724D), Color(0xFF4A99A0), Color(0xFF4959AA)
    )

        val savedRecipe = remember { mutableStateOf(vm.recipesViewModel.userSavedRecipes.contains(actualRecipe.uri.replace("http://www.edamam.com/ontologies/edamam.owl#", "")))}
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

                Image(painter = painterResource(R.drawable.saves), contentDescription = "saves")
                Text(vm.recipesViewModel.selectedRecipeSaves.value.toString()+" saves", fontFamily = Poppins, fontSize = 12.sp)

                Image(painter = painterResource(R.drawable.clock), contentDescription = "time")
                Text(actualRecipe.totalTime.toInt().toString()+" minutes", fontFamily = Poppins, fontSize = 12.sp)


                Image(painter = painterResource(R.drawable.cals), contentDescription = "cals")
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

                    Image(painter = painterResource(R.drawable.bookmark), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {
                                    savedRecipe.value = !savedRecipe.value

                                    var firebase = FirebaseDatabase.getInstance().reference.root

                                    //quitar de savedRecipes

                                    Log.d("savedRecipes", "quitado savedRecipes")

                                    vm.recipesViewModel.userSavedRecipes.remove(
                                        actualRecipe.uri.replace(
                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                            ""
                                        )
                                    )

                                    firebase
                                        .child("Users")
                                        .child(vm.authenticator.currentUID.value.toString())
                                        .child("savedRecipes")
                                        .setValue(vm.recipesViewModel.userSavedRecipes)


                                    if (!vm.recipesViewModel.userSavedRecipes.contains(
                                            actualRecipe.uri.replace(
                                                "http://www.edamam.com/ontologies/edamam.owl#",
                                                ""
                                            )
                                        )
                                    ) {
                                        firebase
                                            .child("EdamamRecipes")
                                            .child(
                                                actualRecipe.uri.replace(
                                                    "http://www.edamam.com/ontologies/edamam.owl#",
                                                    ""
                                                )
                                            )
                                            .child("saves")
                                            .get()
                                            .addOnCompleteListener {
                                                val saved = it.result.value
                                                    .toString()
                                                    .toInt() - 1
                                                Log.d("savedRecipes", "into edamam")
                                                if (saved <= 0) {
                                                    firebase
                                                        .child("EdamamRecipes")
                                                        .child(
                                                            actualRecipe.uri.replace(
                                                                "http://www.edamam.com/ontologies/edamam.owl#",
                                                                ""
                                                            )
                                                        )
                                                        .removeValue()
                                                    Log.d(
                                                        "savedRecipes",
                                                        "quitado recipe de firebase"
                                                    )
                                                } else {
                                                    firebase
                                                        .child("EdamamRecipes")
                                                        .child(
                                                            actualRecipe.uri.replace(
                                                                "http://www.edamam.com/ontologies/edamam.owl#",
                                                                ""
                                                            )
                                                        )
                                                        .child("saves")
                                                        .setValue(saved)


                                                }

                                                Log.d("savedRecipes", "final quitado")


                                            }
                                    }


                                } catch (e: Exception) {
                                    Log.d("savedRecipes", e.message.toString())
                                }


                            }
                            .fillMaxHeight()
                            .weight(1F))

                }
                else{
                    Image(painter = painterResource(R.drawable.bookmark_border), contentDescription = "cals",
                        modifier = Modifier
                            .clickable {

                                try {
                                    savedRecipe.value = !savedRecipe.value

                                    //Meterle a savedRecipes
                                    Log.d("savedRecipes", "meterle savedRecipes")

                                    vm.recipesViewModel.userSavedRecipes.add(
                                        actualRecipe.uri.replace(
                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                            ""
                                        )
                                    )

                                    val firebase = FirebaseDatabase.getInstance().reference.root

                                    firebase
                                        .child("Users")
                                        .child(vm.authenticator.currentUID.value.toString())
                                        .child("savedRecipes")
                                        .setValue(vm.recipesViewModel.userSavedRecipes)

                                    val newRecipe = RecipeCustom()
                                    newRecipe.parseRecipe(actualRecipe)

                                    Log.d("savedRecipes", "recipe Parsed")

                                    firebase
                                        .child("EdamamRecipes")
                                        .child(
                                            actualRecipe.uri.replace(
                                                "http://www.edamam.com/ontologies/edamam.owl#",
                                                ""
                                            )
                                        )
                                        .get()
                                        .addOnCompleteListener {
                                            if (!it.result.exists()) {
                                                firebase
                                                    .child("EdamamRecipes")
                                                    .child(
                                                        actualRecipe.uri.replace(
                                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                                            ""
                                                        )
                                                    )
                                                    .setValue(newRecipe)
                                            } else {
                                                firebase
                                                    .child("EdamamRecipes")
                                                    .child(
                                                        actualRecipe.uri.replace(
                                                            "http://www.edamam.com/ontologies/edamam.owl#",
                                                            ""
                                                        )
                                                    )
                                                    .child("saves")
                                                    .get()
                                                    .addOnCompleteListener {
                                                        if (it.result.value != null && it.result.value != "") {
                                                            val saves = it.result.value
                                                                .toString()
                                                                .toInt()

                                                            firebase
                                                                .child("EdamamRecipes")
                                                                .child(
                                                                    actualRecipe.uri.replace(
                                                                        "http://www.edamam.com/ontologies/edamam.owl#",
                                                                        ""
                                                                    )
                                                                )
                                                                .child("saves")
                                                                .setValue(saves + 1)
                                                        }
                                                    }
                                            }
                                        }
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

        }


}

@Composable
fun TabScreenRecipes(
    tabs: List<String>,
    llistaRecipes: MutableState<MutableList<Hit>>,
    vm: RecipesViewModel,
    navController: NavHostController
) {
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
            ShowRecipes(llistaRecipes, vm, navController)
        }

    }
    else if(showSaved.value){
        Column(
            Modifier
                .fillMaxHeight()
                .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
            ShowSavedRecipes(vm.userSavedRecipes)
        }
    }
}

@Composable
fun ShowSavedRecipes(recipesList : ArrayList<Any>){
    if(recipesList.size > 0){
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
            /*Log.d("recipesList into", llistaRecipes.value.size.toString())
            items(llistaRecipes.value)
            {element ->
                RecipeElement(element.recipe, navController)

            }

            if(listState.firstVisibleItemIndex == llistaRecipes.value.size - 8){
                Log.d("recipesList", "getMore")
                vm.getNextPage()
            }*/
        }
    }
    else{

        Box(
            Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center){

            Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 40.dp)){
                Text("You don't have any saved recipes!", fontFamily = Poppins, textAlign = TextAlign.Center)
            }

        }


    }
}

@Composable
fun TabScreenMeals(recipe : Recipe) {
    var tabIndex = remember { mutableStateOf(0) }

    val showIngredients = remember { mutableStateOf(false)}
    val showNutrition = remember { mutableStateOf(false)}
    val showSteps = remember { mutableStateOf(false)}
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

    if(showIngredients.value){


        Column() {
            ShowRecipeIngredients(recipe.ingredients, recipe.yield.toInt())
        }

    }
    if(showNutrition.value){

        ShowRecipeNutrition(recipe)

    }
    if(showSteps.value){

        Column(
            Modifier
                .fillMaxHeight()
                .background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA))) {
            ShowRecipeSteps()
        }

    }
}

@Composable
fun ShowRecipeIngredients(ingredients : List<Ingredient>, servings : Int){
    Column(Modifier.background(if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFEAEAEA)))
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

                    for (i in 0 until ingredients.size){
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp, end = 20.dp,
                                    bottom = 20.dp
                                )) {
                            Text(ingredients[i].food, Modifier.weight(2F), fontFamily = Poppins, fontWeight = FontWeight.Bold)
                            if(ingredients[i].quantity.roundToInt() > 0){
                                Text(ingredients[i].quantity.roundToInt().toString()+" "+ingredients[i].measure,Modifier.weight(1F),
                                    textAlign = TextAlign.End, fontFamily = Poppins, fontSize = 12.sp)
                            }
                            else{
                                Text("as pleased",Modifier.weight(1F), textAlign = TextAlign.End, fontFamily = Poppins, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }


    }
}
@Composable
fun ShowRecipeSteps(){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(30.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center){

        Box(Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 40.dp)){
            Text("There are no steps available for this recipe :-(", fontFamily = Poppins, textAlign = TextAlign.Center)
        }

    }
}

@Composable
fun ShowRecipeNutrition(recipe : Recipe) {

    val colorsLight = listOf(Color(0xFF6AA73F), Color(0xFFD87F22), Color(0xFFE03535))
    val colorsDark = listOf(Color(0xFF33971D), Color(0xFFE76016), Color(0xFFB81F3E))

    Column(
        Modifier
            .fillMaxHeight()
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
                                            if(isSystemInDarkTheme()){
                                                colorsDark[i%3]
                                            }
                                        else{
                                            colorsLight[i%3]
                                            }
                                                )) {

                                }

                                Text(recipe.digest[i].label.uppercase(), Modifier.padding(start = 5.dp), fontFamily = Poppins )

                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                    Text(recipe.digest[i].daily.toInt().toString()+recipe.digest[i].unit,
                                        fontFamily = Poppins, textAlign = TextAlign.End, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if(i == 2)
                                Divider(Modifier.padding(top=15.dp, bottom=15.dp).border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline)))
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