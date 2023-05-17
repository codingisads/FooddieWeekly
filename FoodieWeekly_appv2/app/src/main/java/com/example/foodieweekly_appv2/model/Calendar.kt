package com.example.foodieweekly_appv2.model

class Calendar () {
    public var currentWeekId : String = ""
    public var ownerUID : String = ""
    public var usersUIDList : MutableList<String> = mutableListOf<String>()
    public var calendarName : String = ""
    var ownerUsername : String = ""

    init {
        
    }


    fun parseCalendar(calendarFromFirebase : HashMap<Any, Any>){
        this.currentWeekId = calendarFromFirebase["currentWeekId"] as String
        this.ownerUID = calendarFromFirebase["ownerUID"] as String
        if( calendarFromFirebase["usersUIDList"] != null){
            this.usersUIDList = calendarFromFirebase["usersUIDList"] as ArrayList<String>
        }

        this.calendarName = calendarFromFirebase["calendarName"] as String
        this.ownerUsername = calendarFromFirebase["ownerUsername"] as String
    }

}