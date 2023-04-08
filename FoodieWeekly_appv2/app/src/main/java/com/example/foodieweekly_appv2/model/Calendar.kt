package com.example.foodieweekly_appv2.model

import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar

class Calendar () {
    public var currentWeekId : String = ""
    public var ownerUID : String = ""
    public var usersUIDList : MutableList<String> = mutableListOf<String>()
    public var calendarName : String = ""

    init {
        
    }



}