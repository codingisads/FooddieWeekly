package com.example.foodieweekly_appv2.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CalendarsObject(

    var selectedCalendarIndex : Int = 0,
    var calendarList : MutableState<MutableList<Calendar>> = mutableStateOf(mutableListOf())
)
