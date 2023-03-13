package viliApp

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.vili.Model.Querys.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var isLogging by savedStateHandle.saveable { mutableStateOf(true) }

    var email by savedStateHandle.saveable { mutableStateOf("") }
    var password by savedStateHandle.saveable { mutableStateOf("") }
    var repeatPassword by savedStateHandle.saveable { mutableStateOf("") }

    var popNContinue by savedStateHandle.saveable { mutableStateOf(false) }

    fun onEmailChange(newValue: String) {
        this.email = newValue
    }

    fun onPasswordChange(newValue: String) {
        this.password = newValue
    }

    fun onRepeatPasswordChange(newValue: String) {
        this.repeatPassword = newValue
    }

    fun turnPopNContinue(){
        popNContinue = !popNContinue
    }

}

