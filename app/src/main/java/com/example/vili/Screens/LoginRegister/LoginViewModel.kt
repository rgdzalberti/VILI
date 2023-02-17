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
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

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

    fun onLogInClick() {

        var errorReturn = 0

        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        if (!email.isNotBlank() || !password.isNotBlank()) {
            errorReturn = 1
            toastID = errorReturn
        } else if (!pattern.matcher(email).matches()) {
            errorReturn = 2
            toastID = errorReturn
        } else{
            //LOGIN
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        try {
                            throw task.exception!!;
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            errorReturn = 7; toastID = errorReturn
                        }

                    } else toastID = errorReturn //Devuelve 0 en Login y cambio de pantalla
                }
        }



    }

    fun onSignUp() {

        var errorReturn = 0
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        if (!email.isNotBlank() || !password.isNotBlank() || !repeatPassword.isNotBlank()) {
            errorReturn = 1
            toastID = errorReturn
        } else if (!pattern.matcher(email).matches()) {
            errorReturn = 2
            toastID = errorReturn
        } else if (password.length < 6) {
            errorReturn = 4
            toastID = errorReturn
        } else if (password != repeatPassword) {
            errorReturn = 3
            toastID = errorReturn
        } else {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        try {
                            throw task.exception!!;
                        } catch (e: FirebaseAuthUserCollisionException) {
                            errorReturn = 6; toastID = errorReturn; Log.i("Login",
                                task.exception.toString()
                            )
                        }

                    } else toastID = errorReturn //Devuelve 0 en Login y cambio de pantalla
                }
        }
    }

    fun disableToast() {
        toastID = -1
    }
}

