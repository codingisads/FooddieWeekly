package com.example.foodieweekly_appv2.pantalles

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.vm

@Composable
fun ShoppingList(){
    BackHandler(enabled = true){

    }


    var tmpList = HashMap<String, String>()
    tmpList = vm.shoppingViewModel.usersShoppingList.value.clone() as HashMap<String, String>

    var tmpChecked = remember { mutableStateOf(false)}

    Column(Modifier
        .background(
            if (isSystemInDarkTheme()) Color(0xFF464646)
            else Color(0xFFEAEAEA))
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {

        Box(Modifier
            .fillMaxSize()
            .padding(20.dp)
            ) {
            Column(Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.surface)
                ,
            verticalArrangement = Arrangement.Center) {

                Text(text = "Your shopping list",
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                if(vm.shoppingViewModel.usersShoppingList.value.size > 0){
                    CheckboxListShopping(tmpList, tmpChecked)

                    Box(Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd){

                        Button(onClick = {
                            tmpChecked.value = false
                                vm.shoppingViewModel.usersShoppingList.value = tmpList
                                vm.shoppingViewModel.addShoppingListToFirebase()
                                tmpList = vm.shoppingViewModel.usersShoppingList.value.clone() as HashMap<String, String>
                             },
                            modifier = Modifier.padding(40.dp)) {

                            Text("UPDATE",
                                fontFamily = Poppins,
                                fontSize = 16.sp)
                        }
                    }
                }
                else{
                    Text(text = "Nothing in your shopping cart, " +
                            "add recipes to the calendar to see the ingredients",
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = Poppins,
                        fontSize = 16.sp
                    )
                }




            }
        }
    }


}

@Composable
fun CheckboxListShopping(tmpList : HashMap<String, String>, checked : MutableState<Boolean>){
    Column(Modifier.fillMaxSize()) {
        var ingredientsList = vm.shoppingViewModel.usersShoppingList.value.keys.toList()
        var quantityUnitsList = vm.shoppingViewModel.usersShoppingList.value.values.toList()
        ingredientsList.forEachIndexed {
                index ,ingredient  ->
            var check = remember { mutableStateOf(checked.value)}


            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = !tmpList.contains(ingredientsList[index]),
                    onCheckedChange =
                    {
                        check.value = it
                        if(check.value){
                            //Si selecciona, elimina de la llista
                            tmpList.remove(ingredientsList[index])
                        }
                        else{
                            //si deselecciona, afegeix
                            tmpList.put(ingredientsList[index], quantityUnitsList[index])
                        }

                    },
                    enabled = true
                )


                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ingredientsList[index],
                        textDecoration =
                        if(!tmpList.contains(ingredientsList[index])) TextDecoration.LineThrough
                        else TextDecoration.None,
                        fontFamily = Poppins,
                        fontSize = 12.sp)

                    Text(quantityUnitsList[index],
                        textDecoration =
                        if(check.value) TextDecoration.LineThrough
                        else TextDecoration.None,
                        fontFamily = Poppins,
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end=5.dp))
                }




            }



        }
    }
}