package com.example.foodieweekly_appv2.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Week {
    public var days = mutableListOf<Day>()



    public fun fillCurrentWeek() : Unit {

        val yesterday = LocalDate.now().minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("E/d", Locale.ENGLISH)
        val formatterDays = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

        val yesterdayFormatted = yesterday.format(formatter)
        val yesterdayFormattedDD = yesterday.format(formatterDays)

        var newDay = Day()
        newDay.dateInDate = yesterdayFormatted;
        newDay.date = yesterdayFormattedDD

        this.days.add(newDay)

        for (i in 0..5){

            val day = Day();
            day.dateInDate = LocalDate.now().plusDays(i.toLong()).format(formatter)
            day.date = LocalDate.now().plusDays(i.toLong()).format(formatterDays)

            this.days.add(day)

        }
    }

}