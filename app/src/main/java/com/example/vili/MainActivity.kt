package viliApp

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import viliApp.NavigationHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                startApp()


        }
    }

}


@Composable
fun startApp() {

    //Chekeo si el usuario ya está iniciado para no llevarlo a la pantalla de login

    //Esto retorna null si el usuario no está logeado
    val user = FirebaseAuth.getInstance().currentUser
    var startDestination = ""

    if (user == null){
        startDestination = Destinations.Pantalla1.ruta
    } else startDestination = Destinations.Pantalla2.ruta

   val navController = rememberNavController()
   NavigationHost(navController,startDestination)


}