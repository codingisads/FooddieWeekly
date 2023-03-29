package com.example.foodieweekly_appv2.pantalles

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodieweekly_appv2.firebase.Authenticator
import com.example.foodieweekly_appv2.ui.theme.Poppins
import com.example.foodieweekly_appv2.utils.TabScreen


@Composable
fun PantallaPrincipal(authenticator : Authenticator){

    var dies = listOf<String>("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    var diesNum = listOf<Int>(13, 14, 15, 16, 17, 18, 19)

    var username = remember { mutableStateOf("stranger")}
    authenticator.getUserUsername(username)

    // Initialize Calendar service with valid OAuth credentials
    // Initialize Calendar service with valid OAuth credentials

    Column (
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)) {

        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Outlined.AccountCircle,
                modifier = Modifier
                    .size(40.dp),
                contentDescription = "drawable icons",
                tint = Color.Unspecified
            )
        }

        Box {
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = "Good morning, " + username.value + "!",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = Poppins
                )


                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Friday 17th of February, 2023",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.outline)
                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 15.dp)) {
                        Icon(
                            Icons.Rounded.Share,
                            modifier = Modifier
                                .size(30.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )
                    }




                }


                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)) {
                    for (i in 0 until dies.count()){
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .width(40.dp)
                                .height(50.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant), Alignment.Center){
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    dies[i],
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Light,
                                )
                                Text(
                                    diesNum[i].toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Light,
                                )
                            }

                        }
                    }
                }

            }

        }

        Box(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight()) {
            Column(Modifier.fillMaxWidth()) {
                Text(text = "Your calories today",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = Poppins
                )

                Box(
                    Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(), contentAlignment = Alignment.Center){

                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = "1000/2000 kcals",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = Poppins,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        CircularProgressIndicator(progress = 0.5f, modifier = Modifier.size(50.dp), color = MaterialTheme.colorScheme.primary)
                        Text(text = "50% of your daily goal!",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = Poppins, fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(top = 10.dp)
                        )

                    }

                }

            }


        }

        TabScreen()

    }

}




@Composable
fun CustomTabs() {
    var selectedIndex = remember { mutableStateOf(0) }

    val list = listOf("Active", "Completed")

    TabRow(selectedTabIndex = selectedIndex.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        indicator = { tabPositions: List<TabPosition> ->
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex.value == index
            Tab(
                modifier = if (!selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.LightGray
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.primary
                    ),
                selected = selected,
                onClick = { selectedIndex.value = index },
                text = { Text(text = text, color = Color(0xff6FAAEE)) }
            )
        }
    }
}

