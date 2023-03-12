package viliApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.vili.myApp.theme.VILITheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            VILITheme() {
                startApp()
            }

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
    } else startDestination = Destinations.MainScreen.ruta

   val navController = rememberNavController()
   NavigationHost(navController,startDestination)


}