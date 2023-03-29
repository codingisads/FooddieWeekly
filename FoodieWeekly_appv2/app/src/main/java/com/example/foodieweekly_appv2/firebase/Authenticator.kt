package com.example.foodieweekly_appv2.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.model.User
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.viewmodel.LoginViewModel
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*


class Authenticator {
    fun finishLogin(task: Task<GoogleSignInAccount> , alreadyInDB : MutableState<Boolean> , showSignupConfig: MutableState<Boolean>) {

        try {
            val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)

            account?.idToken?.let {
                token ->
                val auth = FirebaseAuth.getInstance()
                val credencial = GoogleAuthProvider.getCredential(token, null)

                auth.signInWithCredential(credencial).addOnCompleteListener {
                    tarea ->
                        if(tarea.isSuccessful){
                            var user = auth.currentUser
                            Log.d("autenticacion google", "Obteniendo user")
                            if (user != null) {

                                Log.d("autenticacion google", user.uid.toString())
                                user.displayName?.let { Log.d("autenticacion google", it) }

                                loggedIn.value = true

                                var db = RealtimeDatabase()

                                db.checkIfUserUIDIsRegistered(getUserUID(), alreadyInDB, showSignupConfig)


                                Log.d("checkIfUserUIDIsRegistered", "Autenticando")

                            }
                            else{

                                loggedIn.value = false
                                Log.d("autenticacion google", "User nulo")
                            }
                        }
                    else{
                        Log.d("autenticacion google", "ALGO MAL")
                    }
                }
            }

            Log.d("autenticacion google", "TODO OK")
        }
        catch(e : Exception){
            Log.d("autenticacion google", e.message.toString())
        }
    }

    //Setup

    private val fire : FirebaseAuth;

    init {
        fire = FirebaseAuth.getInstance()

    }

    var _registered = mutableStateOf("")
    public val registered = _registered

    var _hasRegistered = mutableStateOf(false)
    public val hasRegistered = _hasRegistered

    var _loggedIn = mutableStateOf(false)
    public val loggedIn = _loggedIn

    var _alreadyRegisteredEmail = mutableStateOf(false)
    public val alreadyRegisteredEmail = _alreadyRegisteredEmail


    var _currentUsername = mutableStateOf("")
    public val currentUsername = _currentUsername



    @OptIn(DelicateCoroutinesApi::class)
    public val signup = fun(email: String, passw : String, user : User, navController : NavHostController, loginvm : LoginViewModel) : Unit {

        try{

                fire.createUserWithEmailAndPassword(email, passw).addOnCompleteListener {

                    if(it.isSuccessful){
                        Log.d("FIREBASE 1", it.result.user?.email.toString())
                        registered.value = "ok"
                        Log.d("FIREBASE 2", registered.value)

                        hasRegistered.value = true

                        var userUID : String? = null

                        login(email, passw, navController, loginvm)


                        Log.d("FIREBASE creating on db", "creating")
                        var db = RealtimeDatabase()
                        //db.createUserOnDB(,user)

                        navController.navigate(Destinations.PantallaPrincipal.ruta)


                        Log.d("FIREBASE 3 despues log", loggedIn.value.toString())



                    }
                    else{
                        val exc = it.exception
                        Log.d("FIREBASE EXC 1", exc?.message.toString())

                        registered.value = exc?.message.toString()
                        hasRegistered.value = false

                        Log.d("FIREBASE EXC 2", registered.value)
                    }

                }



        }
        catch (er : FirebaseAuthUserCollisionException){
            Log.d("FIREBASE ERROR", er.message.toString())
            registered.value = er.message.toString()
        }


    }

    public val checkIfEmailIsNotRegistered = fun(email : String,
                                                 vm : SignupViewModel,
    navController : NavHostController) {

        fire.fetchSignInMethodsForEmail(email).addOnCompleteListener {

            var res = it.result.signInMethods

            if (res != null) {
                alreadyRegisteredEmail.value = res.isNotEmpty()


                vm.showDialog.value = alreadyRegisteredEmail.value
                vm.goToUserPreferences.value = !alreadyRegisteredEmail.value

                if (vm.goToUserPreferences.value) {
                    navController.navigate(Destinations.SignupConfig.ruta)
                }
                else{
                    registered.value = "This email is already registered!"
                }
            }
            else
            {
                alreadyRegisteredEmail.value = false
                vm.showDialog.value = alreadyRegisteredEmail.value
                vm.goToUserPreferences.value = false
            }
        }
    }

    public val login = fun(email: String, passw : String, navController : NavHostController, vm : LoginViewModel) : Unit {
        try{
            Log.d("Entro", "Entro")

            runBlocking {
                fire.signInWithEmailAndPassword(email, passw).addOnCompleteListener {
                    if(it.isSuccessful){
                        it.result.user?.uid?.let { it1 -> Log.d("FIREBASE 1 en login", it1) }
                        loggedIn.value = true
                        Log.d("FIREBASE 2 / logged", loggedIn.value.toString())

                        if(loggedIn.value) {
                            goToMainActivity(navController)
                        }
                        else{
                            vm.showDialog.value = true
                        }
                    }
                    else{
                        val exc = it.exception
                        Log.d("FIREBASE EXC 1", exc?.message.toString())

                        loggedIn.value = false
                        vm.showDialog.value = true

                        Log.d("FIREBASE EXC 2", loggedIn.value.toString())
                    }
                }
            }




        }
        catch (er : FirebaseAuthUserCollisionException){
            Log.d("FIREBASE ERROR", er.message.toString())

        }
    }

    public val loginGoogle = fun(activity : Activity) : Unit {
        val google = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(activity, google )

        val signInIntent : Intent = googleClient.signInIntent
        Log.d("autenticacion google", "signInIntent")
        activity.startActivityForResult(signInIntent, 1)


    }



    public val goToMainActivity = fun (navController : NavHostController) : String {
        var database = FirebaseDatabase.getInstance().reference

        var uid = getUserUID()


        if(uid != null){
            database.root.child("Users").child(uid).child("username").get().addOnCompleteListener {
                val res = it.result.value
                Log.d("RESULT ->", res.toString())

                if(res != null){
                    currentUsername.value = res.toString();
                    navController.navigate(Destinations.PantallaPrincipal.ruta)
                }
                else{
                    currentUsername.value = ""
                }

            }

            return currentUsername.value;

        }
        else
            return "nul"




    }

    public val getUserUID = fun() : String? {
        try {


            Log.d("FIREBASE GETTING UID", fire.currentUser?.uid.toString())
            return fire.currentUser?.uid.toString()

        } catch (er: FirebaseAuthUserCollisionException) {
            Log.d("FIREBASE ERROR", er.message.toString())
            return "null"
        }
    }

    public val getUserUsername = fun(username : MutableState<String>) : Unit {
        var database = FirebaseDatabase.getInstance().reference
        var uid = getUserUID()


        if(uid != null){

            Log.d("getUserUsername", uid)
            database.root.child("Users").child(uid).child("username").get().addOnCompleteListener {
                val res = it.result.value
                Log.d("RESULT ->", res.toString())

                if(res != null){
                    username.value = res.toString();

                }
                else{
                    username.value = "stranger"
                }

            }

            Log.d("getUserUsername", username.value)

        }
        else
            username.value = "stranger"


    }




}



