package com.example.foodieweekly_appv2.firebase

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.model.User
import com.example.foodieweekly_appv2.navigation.Destinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RealtimeDatabase {

    init {

    }

    private var _usernameExists = mutableStateOf(false)
    public val usernameExists = _usernameExists


    public val checkIfUsernameExists = fun(username : String){

        var database = FirebaseDatabase.getInstance().reference

        database.root.child("UsersUsernames").child(username).get()
            .addOnSuccessListener {

                usernameExists.value = it.exists()
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
                Log.d("FIREBASE REALTIME NEW", userUID)
            }


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
                    .addOnFailureListener {

                    }

            }





        } catch (er: FirebaseAuthUserCollisionException) {
            Log.d("FIREBASE ERROR", er.message.toString())
        }
    }

    public var checkIfUserUIDIsRegistered = fun(uid : String?, userExists : MutableState<Boolean>, showSignupConfig: MutableState<Boolean>) : Unit {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference


        try {
            if (uid != null) {
                database.root.child("Users").child(uid).get()
                    .addOnSuccessListener {
                        userExists.value = it.exists()
                        showSignupConfig.value = !userExists.value

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