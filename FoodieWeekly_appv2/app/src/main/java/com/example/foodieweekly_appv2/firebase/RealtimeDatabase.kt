package com.example.foodieweekly_appv2.firebase

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.model.Calendar
import com.example.foodieweekly_appv2.model.User
import com.example.foodieweekly_appv2.model.Week
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class RealtimeDatabase {

    init {

    }

    private var _usernameExists = mutableStateOf(false)
    public val usernameExists = _usernameExists


    public val checkIfUsernameExists = fun(username : String, checked : MutableState<Boolean>){

        var database = FirebaseDatabase.getInstance().reference

        database.root.child("UsersUsernames").child(username).get()
            .addOnSuccessListener {

                usernameExists.value = it.exists()
                checked.value = true
            }
    }


    public val createUserOnDB = fun (auth : Authenticator, user : User) : Unit {
        try {
            // Write a message to the database
            // ...


            Log.d("FIREBASE on db", "creating user")
            var database: DatabaseReference = FirebaseDatabase.getInstance().reference


            var userUID = auth.getUserUID()
            Log.d("FIREBASE REALTIME NEW", "hOLA")


            if (userUID != null) {
                //Checks if username is not registered in DB UsersUsernames
                database.root.child("UsersUsernames").child(user.username).get()
                    .addOnSuccessListener {

                        if(it.exists()){
                            Log.d("FIREBASE REALTIME", "Username already exists")
                        }
                        else
                        {
                            FirebaseDatabase.getInstance().reference.root.child("Users").child(userUID).setValue(user)
                            FirebaseDatabase.getInstance().reference.root.child("UsersUsernames").child(user.username).setValue(userUID)


                        }
                    }

            }





        } catch (er: FirebaseAuthUserCollisionException) {
            Log.d("FIREBASE ERROR", er.message.toString())
        }
    }

    public val createCalendarOnDB = fun(uid : String, name : String, navController : NavHostController , auth : Authenticator) : Unit {

        try{
            FirebaseDatabase.getInstance().reference.root.child("Weeks").get().addOnCompleteListener {


                val newWeek = Week()
                newWeek.fillCurrentWeek()
                newWeek.weekId = it.result.childrenCount.toString()

                val newCalendar = Calendar()
                newCalendar.ownerUID = uid;
                newCalendar.calendarName = name;
                newCalendar.currentWeekId = newWeek.weekId;

                FirebaseDatabase.getInstance().reference.root.child("Weeks").child(it.result.childrenCount.toString())
                    .setValue(newWeek).addOnCompleteListener {
                        Log.d("createCalendarOnDB", "adding")
                        //Log.d("createCalendarOnDB", newCalendar.ownerUID)

                        FirebaseDatabase.getInstance().reference.root.child("Calendars").get().addOnCompleteListener {
                            val index = it.result.childrenCount

                            FirebaseDatabase.getInstance().reference.root.child("Calendars").
                            child(index.toString()).setValue(newCalendar)
                            Log.d("createCalendarOnDB", "done")

                            addCalendarToUser(index.toString(), uid, navController, auth)
                        }
                    }

            }




        }
        catch (e : Exception)
        {
            Log.d("createCalendarOnDB", e.message.toString())
        }

    }


    public val addCalendarToUser = fun(calendarId : String, uid : String, navController: NavHostController, auth : Authenticator) : Unit {

        Log.d("addCalendarToUser", "Calendar ID " + calendarId)
        try{
            FirebaseDatabase.getInstance().reference.root.child("Users").child(uid)
                .child("calendarIdList").get().addOnCompleteListener {
                    val ids = it.result.children

                    Log.d("addCalendarToUser", "Calendar ID " + calendarId)
                    Log.d("addCalendarToUser", it.result.childrenCount.toString())
                    val calendarIdList = mutableListOf<String>()

                    ids.forEach {
                        id ->
                        calendarIdList.add(id.value.toString())
                        Log.d("addCalendarToUser", id.value.toString())
                    }

                    calendarIdList.add(calendarId);
                    Log.d("addCalendarToUser", uid)

                    FirebaseDatabase.getInstance().reference.root.child("Users").child(uid)
                        .child("calendarIdList").setValue(calendarIdList).addOnCompleteListener {

                            Log.d("addCalendarToUser", "done")
                            auth.goToMainActivity(navController)

                        }


            }

        }
        catch(e : Exception){
            Log.d("addCalendarToUser", e.message.toString())
        }

    }

    public fun changeDay(uid : String, calendarId : String) : Unit {
        FirebaseDatabase.getInstance().reference.root.child("Users").child(uid)
            .child("calendarIdList").get().addOnCompleteListener {
                var calendar = it.result.children

                //var firstCalId = calendar.first().value

                //Log.d("changeDay", "firstCalId " + firstCalId)

                FirebaseDatabase.getInstance().reference.root.child("Calendars").child(calendarId)
                    .child("currentWeekId")
                    .get().addOnCompleteListener {
                            cal ->

                        var weekId = cal.result.value

                        FirebaseDatabase.getInstance().reference.root.child("Weeks").child(weekId.toString())
                            .child("days").get()
                            .addOnCompleteListener {
                                day ->

                                val res = day.result.value as ArrayList<Any>

                                val res1 = res.get(1) as java.util.HashMap<*, *>

                                Log.d("changeDay", "res1 + " + res1.get("date"))

                                val date = res1.get("date").toString().replace('/', '-')

                                val formatter = DateTimeFormatter.ofPattern("E/d", Locale.ENGLISH)
                                val formatterDays = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

                                val dateNow = LocalDate.now()
                                val dateLate = LocalDate.parse(date as CharSequence?)

                                Log.d("changeDay", "dateLate + " + dateLate.toString())

                                val diference = dateLate.until(dateNow, ChronoUnit.DAYS)

                                Log.d("changeDay", "diference + " + diference.toString())

                                val latestDate = dateLate.plusDays(5);
                                Log.d("changeDay", "latestDate +" + latestDate.toString())

                                //if(LocalDate.now().minusDays(LocalDate.from(res2)))


                                val newArray = res.toMutableList()

                                for(i in 0 until diference.toInt()){
                                    newArray.removeAt(0)

                                    val toAddDate = latestDate.plusDays(i.toLong() + 1);
                                    Log.d("changeDay", "toAddDate + "+ toAddDate.toString())

                                    val newEntry = mutableMapOf<String, String>()
                                    newEntry["date"] = toAddDate.format(formatterDays)
                                    newEntry["dateInDate"] = toAddDate.format(formatter)
                                    newArray.add(newEntry)
                                }
                                Log.d("changeDay", "adding")

                                if(!newArray.containsAll(res)){
                                    FirebaseDatabase.getInstance().reference.root.child("Weeks").child(weekId.toString())
                                        .child("days").setValue(newArray).addOnCompleteListener {
                                            Log.d("changeDay", "done")
                                        }
                                }



                            }





                    }

            }
    }

    public fun getWeekDateInDate(calendarId: String, dies: MutableList<String>, diesNum: MutableList<String>) : Unit{


        Log.d("getWeekDateInDatefun", calendarId)

        FirebaseDatabase.getInstance().reference.root.child("Calendars").child(calendarId).child("currentWeekId")
            .get().addOnCompleteListener {
                currentWeekId ->
                Log.d("getWeekDateInDatefun", "weekId->" + currentWeekId.result.value.toString())
                FirebaseDatabase.getInstance().reference.root.child("Weeks")
                    .child(currentWeekId.result.value.toString()).child("days").get().addOnCompleteListener {
                        days ->
                        dies.clear()
                        diesNum.clear()

                        for (i in 0 until 7){
                            val child = days.result.child(i.toString()).child("dateInDate").value
                            Log.d("getWeekDateInDatefun", "child: " + child.toString())

                            val childSeparated = child.toString().split('/')
                            dies.add(childSeparated[0])
                            Log.d("getWeekDateInDatefun", childSeparated[0])
                            diesNum.add(childSeparated[1])
                            Log.d("getWeekDateInDatefun", childSeparated[1])
                        }

                    }



                Log.d("getWeekDateInDatefun", "Len " + diesNum.count().toString())
            }
    }

    public fun getCalendarId(uid : String, calId : MutableState<String>, calInex : Int = 0) : Unit {

        Log.d("getFirstCalendarId", "uid -> " +uid)
        FirebaseDatabase.getInstance().reference.root.child("Users").child(uid)
            .child("calendarIdList").get().addOnCompleteListener {
                var calendar = it.result.children

                if(it.result.childrenCount >= 1){
                    var firstCalId = calendar.elementAt(calInex).value
                    Log.d("getFirstCalendarId", firstCalId.toString())

                    calId.value = firstCalId.toString();
                }


            }
    }

    public fun getCalendarWeekId(uid : String, calId : MutableState<String>, weekId : MutableState<String>){
        getCalendarId(uid , calId)

        if(calId.value != ""){
            FirebaseDatabase.getInstance().reference.root.child("Calendars").child(calId.value)
                .child("currentWeekId").get().addOnCompleteListener {
                    weekId.value = it.result.value.toString()
                }
        }
    }

    public var checkIfUserUIDIsRegistered = fun(uid : String?, userExists : MutableState<Boolean>,
                                                showSignupConfig: MutableState<Boolean>) : Unit {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference


        try {
            if (uid != null) {
                database.root.child("Users").child(uid).get()
                    .addOnSuccessListener {
                        userExists.value = it.exists()
                        showSignupConfig.value = !userExists.value

                        Log.d("checkIfUserUIDIsRegistered", "userExists " + userExists.value.toString())
                        Log.d("checkIfUserUIDIsRegistered", "showSignupConfig " + showSignupConfig.value.toString())
                        Log.d("checkIfUserUIDIsRegistered", it.key.toString())



                    }
            }
        } catch(e : Exception){
            userExists.value = false
            showSignupConfig.value = false
            Log.d("checkIfUserUIDIsRegistered", "Something went wrong here")
        }
    }


}