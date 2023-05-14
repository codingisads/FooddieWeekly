package com.example.foodieweekly_appv2.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.example.foodieweekly_appv2.R
import com.example.foodieweekly_appv2.model.User
import com.example.foodieweekly_appv2.model.enums.TypeOfSingup
import com.example.foodieweekly_appv2.navigation.Destinations
import com.example.foodieweekly_appv2.viewmodel.SignupViewModel
import com.example.foodieweekly_appv2.vm
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking

class Authenticator {



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


    var _currentUID = mutableStateOf("");
    public val currentUID = _currentUID;


    public val signup = fun(email: String, passw : String, user : User, navController : NavHostController, vm : SignupViewModel) : Unit {

        try{
            if(vm.signupType.value == TypeOfSingup.BASIC){
                fire.createUserWithEmailAndPassword(email, passw).addOnCompleteListener {

                    if(it.isSuccessful){

                        registered.value = "ok"

                        hasRegistered.value = true

                        var db = RealtimeDatabase()
                        db.createUserOnDB(this,user)


                        fire.signInWithEmailAndPassword(email, passw).addOnCompleteListener {
                            if(it.isSuccessful){

                                loggedIn.value = true
                                vm.showDialog.value = false

                                currentUID.value = getUserUID().toString()

                                db.createCalendarOnDB(currentUID.value, "Main Calendar", navController, this, true)

                            }
                            else{

                                loggedIn.value = false
                                vm.showDialog.value = true

                            }
                        }
                    }
                    else{
                        val exc = it.exception

                        registered.value = exc?.message.toString()

                        hasRegistered.value = false

                    }

                }
            }
            else{
                registered.value = "ok"

                hasRegistered.value = true

                var db = RealtimeDatabase()

                db.createUserOnDB(this,user)

                db.createCalendarOnDB(currentUID.value, "Main Calendar", navController, this, true)

                loggedIn.value = true
                vm.showDialog.value = false
            }
        }
        catch (er : FirebaseAuthUserCollisionException){
            registered.value = er.message.toString()
        }


    }

    public val login = fun(email: String, passw : String) : Unit {

        val navController = vm.navController
        val mainVm = vm
        val vm = vm.loginViewModel

        try{

            runBlocking {
                com.example.foodieweekly_appv2.vm.loginViewModel.loading.value = true
                fire.signInWithEmailAndPassword(email, passw).addOnCompleteListener {
                    if(it.isSuccessful){

                        loggedIn.value = true

                        currentUID.value = getUserUID().toString()

                        if(!currentUID.value.isNullOrEmpty()){
                            goToMainActivity(navController)
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

    fun finishLogin(task: Task<GoogleSignInAccount>) {


        Log.d("autenticacion google credential", "finishLogin")
        try {
            runBlocking{
                vm.loginViewModel.loading.value = true

                Log.d("autenticacion google credential", "finishLogin")
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)


                Log.d("autenticacion google credential", account.toString())

                account?.idToken?.let {
                        token ->
                    val auth = FirebaseAuth.getInstance()
                    val credencial = GoogleAuthProvider.getCredential(token, null)

                    Log.d("autenticacion google credential", credencial.toString())

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

                                currentUID.value = getUserUID().toString()


                                if(currentUID.value != ""){
                                    FirebaseDatabase.getInstance().reference.root.child("Users")
                                        .child(currentUID.value).get()
                                        .addOnSuccessListener {

                                            //goToMainActivity(vm.navController);
                                            db.checkIfUserUIDIsRegistered2(currentUID.value)

                                        }
                                }



                                //db.createCalendarOnDB(currentUID.value, "Main Calendar")


                                //Prova()
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


        }
        catch(e : ApiException){
            Log.d("autenticacion google error", e.stackTraceToString())
        }
    }


    public val goToMainActivity = fun (navController : NavHostController) : Unit {

        var uid = getUserUID()


        if(uid != null){
            vm.recipesViewModel.getUserSavedRecipesIds()

            vm.pantallaPrincipalViewModel.settingUp();


            /*vm.pantallaPrincipalViewModel.getMealsFromDay(vm.pantallaPrincipalViewModel.weekId.value,
                vm.pantallaPrincipalViewModel.selectedIndex.value)*/

        }
    }

    public val checkIfEmailIsNotRegistered = fun(email : String) {

        fire.fetchSignInMethodsForEmail(email).addOnCompleteListener {

            var res = it.result.signInMethods

            if (res != null) {
                alreadyRegisteredEmail.value = res.isNotEmpty()


                vm.signupViewModel.showDialog.value = alreadyRegisteredEmail.value
                vm.signupViewModel.goToUserPreferences.value = !alreadyRegisteredEmail.value

                if (vm.signupViewModel.goToUserPreferences.value) {
                    vm.navController.navigate(Destinations.SignupConfig.ruta)
                }
                else{
                    registered.value = "This email is already registered!"
                }
            }
            else
            {
                alreadyRegisteredEmail.value = false
                vm.signupViewModel.showDialog.value = alreadyRegisteredEmail.value
                vm.signupViewModel.goToUserPreferences.value = false
            }
        }
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

            Log.d("getUserUsername uid 1", uid)
            database.root.child("Users").child(uid).child("username").get().addOnCompleteListener {
                val res = it.result.value
                Log.d("RESULT ->", res.toString())

                if(res != null){
                    username.value = res.toString();

                }
                else{
                    username.value = "friend"
                }

            }

            Log.d("getUserUsername", username.value)

        }
        else
            username.value = "friend"


    }


}



