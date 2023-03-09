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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

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

    fun onLogInClick(context: Context){
        try {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthUserCollisionException -> "Correo electrónico ya registrado"
                        is FirebaseAuthWeakPasswordException -> "Contraseña demasiado débil"
                        is FirebaseAuthInvalidCredentialsException -> "Credenciales no válidas"
                        else -> "Error al crear la cuenta"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                .addOnSuccessListener { task ->
                    Toast.makeText(context, "Logeado con éxito", Toast.LENGTH_SHORT).show()
                    popNContinue = true
                }
        } catch (error: Exception) {
            Toast.makeText(context, "No dejes campos vacios", Toast.LENGTH_SHORT).show()
        }
    }

    fun onSignUp(context: Context) {
        try {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthUserCollisionException -> "Correo electrónico ya registrado"
                        is FirebaseAuthWeakPasswordException -> "Contraseña demasiado débil"
                        is FirebaseAuthInvalidCredentialsException -> "Credenciales no válidas"
                        else -> "Error al crear la cuenta"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                .addOnSuccessListener { task ->
                    Toast.makeText(context, "Registrado con éxito", Toast.LENGTH_SHORT).show()
                    popNContinue = true
                }
        } catch (error: java.lang.Exception) {
            Toast.makeText(context, "No dejes campos vacios", Toast.LENGTH_SHORT).show()
        }

    }

}

