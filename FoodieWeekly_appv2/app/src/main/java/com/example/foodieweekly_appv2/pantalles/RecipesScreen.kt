package com.example.foodieweekly_appv2.pantalles

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.ImagePainter
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.model.recipesApi.Hit
import com.example.foodieweekly_appv2.model.recipesApi.Recipe
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.OutlinedTextFieldCustom
import com.example.foodieweekly_appv2.utils.TabScreenRecipes
import com.example.foodieweekly_appv2.viewmodel.RecipesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(llistaRecipes : MutableState<MutableList<Hit>>, vm: RecipesViewModel) {

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
                leadingIcon = { Icon(
                    if(isSystemInDarkTheme())
                        painterResource(R.drawable.filter_alt_white_48dp)
                    else
                        painterResource(R.drawable.filter_alt_black_48dp), "", modifier = Modifier.wrapContentWidth())},
                trailingIcon = { Icon(Icons.Rounded.Search, "")},
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                singleLine = true
            )
        }


        TabScreenRecipes(listOf("All", "Saved", "Mine"), { ShowRecipes(llistaRecipes, vm) })
    }


}

@Composable
fun ShowRecipes(llistaRecipes : MutableState<MutableList<Hit>>, vm: RecipesViewModel) {

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
            RecipeElement(element.recipe)

        }

        if(listState.firstVisibleItemIndex == llistaRecipes.value.size - 8){
            Log.d("recipesList", "getMore")
            vm.getNextPage()
        }
    }
}


@Composable
fun RecipeElement(recipe : Recipe) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {

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

private fun retallaText(text: String, mida: Int) = if (text.length <= mida) text else {
    val textAmbEllipsis = text.removeRange(startIndex = mida, endIndex = text.length)
    "$textAmbEllipsis..."
}