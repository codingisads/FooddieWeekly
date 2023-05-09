package com.example.foodieweekly_appv2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {


    //LOGIN



    private var _email = mutableStateOf("");
    public val email = _email

    private var _password = mutableStateOf("");
    public val password = _password;


    private var _showDialog = mutableStateOf(false);
    public val showDialog = _showDialog;

    private var _validEmail = mutableStateOf(true)
    public val validEmail = _validEmail

    private var _validPassword = mutableStateOf(true)
    public val validPassword = _validPassword

    private var _loading = mutableStateOf(false)
    val loading = _loading

}

