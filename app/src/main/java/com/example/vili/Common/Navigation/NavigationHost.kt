package viliApp

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationHost (navController: NavHostController,startDestination: String){
    NavHost(
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(Destinations.Pantalla1.ruta){
           LoginScreen(navController = navController)
        }
        composable(Destinations.Pantalla2.ruta){

            HomeScreen(navController = navController)
        }
        composable(route = Destinations.Pantalla3.ruta){
            //mainScreen(navController)
        }

    }
}

//Uso esta función para eliminar la pantalla de registro de la pila. Una vez inicias sesion o te registras no puedes volver a esta
//pantalla sin deslogearte antes
@Composable
fun NavigatePop(navController:NavController,destination:String){
    navController.navigate(destination) {
        popUpTo(Destinations.Pantalla1.ruta) {
            inclusive = true
        }
    }
}







