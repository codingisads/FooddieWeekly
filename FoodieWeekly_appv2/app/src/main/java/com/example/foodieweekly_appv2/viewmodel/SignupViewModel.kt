package com.example.foodieweekly_appv2.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodieweekly_appv2.model.User
import com.example.foodieweekly_appv2.model.UserHealthCondition
import com.example.foodieweekly_appv2.model.enums.TypeOfSingup
import java.text.SimpleDateFormat
import java.time.LocalDate;
import java.time.Period
import java.util.*

class SignupViewModel : ViewModel() {

    private var _email = mutableStateOf("");
    public val email = _email

    private var _password = mutableStateOf("");
    public val password = _password;

    private var _username = mutableStateOf("");
    public val username = _username;

    private var _showDialog = mutableStateOf(false);
    public val showDialog = _showDialog;

    private var _validEmail = mutableStateOf(true)
    public val validEmail = _validEmail

    private var _validPassword = mutableStateOf(true)
    public val validPassword = _validPassword

    private var _user = User()
    public val user = _user

    private var _userHealth = UserHealthCondition()
    public val userHealth = _userHealth


    var _goToUserPreferences = mutableStateOf(false)
    public val goToUserPreferences = _goToUserPreferences

    val signupType = mutableStateOf(TypeOfSingup.BASIC)

    public var calculateCalories = fun() : Int{
        val TBM : Double
        if(userHealth.isMale){
           TBM =
                (10 * userHealth.weight.toFloat()) + (6.25 * userHealth.height.toFloat()) - (5 * calculateAge()) + 5

        }
        else{
            TBM = (10 * userHealth.weight.toFloat()) + (6.25 * userHealth.height.toFloat()) - (5 * calculateAge()) + 161
        }

        when(userHealth.activeLevel){
            0 -> return (TBM * 1.2).toInt()
            1 -> return (TBM * 1.375).toInt()
            2 -> return (TBM * 1.55).toInt()
            3 -> return (TBM * 1.725).toInt()
            else -> return -1
        }


    }

    public var calculateAge =
    fun() : Int{
        val date = userHealth.birthdate.split('-')

        Log.d("EDAD DIA -> ", date[0])
        Log.d("EDAD -> MES", date[1])
        Log.d("EDAD -> AÑO", date[2])

        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(date[2].toInt(), date[1].toInt(), date[0].toInt())

        Log.d("EDAD -> DOB", dob.toString())

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)


        Log.d("EDAD -> ", age.toString())

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age


        /*return Period.between(
            LocalDate.of(date.get(2).toInt(), date.get(2).toInt(), date.get(2).toInt()),
            LocalDate.now()).years*/

    }
}