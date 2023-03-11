package com.example.vili.Model.Querys

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class FBAuth {
    companion object {

        //region LOGIN
        fun onLogIn(email: String, password: String, context: Context, callback: (Boolean) -> Unit) {

            try {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnFailureListener { exception ->
                        val errorMessage = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Credenciales no válidas"
                            else -> "Error"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                    //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                    .addOnSuccessListener { task ->
                        Toast.makeText(context, "Logeado con éxito", Toast.LENGTH_SHORT).show()
                        callback(true)
                    }
            } catch (error: Exception) {
                Log.i("wawa","aaaaaa")
                Toast.makeText(context, "No dejes campos vacios", Toast.LENGTH_SHORT).show()
                callback(false)
            }

        }
        //endregion

        //region SIGNUP
        fun onSignUp(email: String, password: String, context: Context, callback: (Boolean) -> Unit) {

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
                        callback(false)
                    }
                    //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                    .addOnSuccessListener { task ->
                        Toast.makeText(context, "Registrado con éxito", Toast.LENGTH_SHORT).show()
                        callback(true)
                    }
            } catch (error: java.lang.Exception) {
                Toast.makeText(context, "No dejes campos vacios", Toast.LENGTH_SHORT).show()
                callback(false)
            }

        }
        //endregion






    }
}