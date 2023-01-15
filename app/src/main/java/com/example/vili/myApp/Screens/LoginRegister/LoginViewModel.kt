package viliApp

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel() {

    var email by savedStateHandle.saveable { mutableStateOf("") }
    var password by savedStateHandle.saveable { mutableStateOf("") }
    var repeatPassword by savedStateHandle.saveable { mutableStateOf("") }

    //Para ser observado y mostrar toast
    var toastID by savedStateHandle.saveable { mutableStateOf(-1) }


    //var context = context

    fun onEmailChange(newValue: String) {
        this.email = newValue
    }

    fun onPasswordChange(newValue: String) {
        this.password = newValue
    }

    fun onRepeatPasswordChange(newValue: String) {
        this.repeatPassword = newValue
    }

    fun onLogInClick(){

        var errorReturn = 0

        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        if (!email.isNotBlank() || !password.isNotBlank()){
            errorReturn = 1
        }
        else if (!pattern.matcher(email).matches()) {
            errorReturn = 2
        }

        //if no existe en la database este usuario no existe 3

        //Si no hay errores se logea
        if (errorReturn == 0){
            //LOGIN
        }

        toastID = errorReturn


    }

    fun onSignUp(){

        var errorReturn = 0
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        if (!email.isNotBlank() || !password.isNotBlank() || !repeatPassword.isNotBlank()){
            errorReturn = 1
        }
        else if (!pattern.matcher(email).matches()) {
            errorReturn = 2
        }
        else if (password != repeatPassword){
            errorReturn = 3
        }

        if (errorReturn == 0){
            //REGISTER
        }

        toastID = errorReturn


    }

    fun disableToast(){
        toastID = -1
    }
}

